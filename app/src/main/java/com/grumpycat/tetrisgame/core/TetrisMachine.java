package com.grumpycat.tetrisgame.core;


import android.graphics.Canvas;
import android.graphics.Rect;

import com.grumpycat.tetrisgame.GameConfig;

import org.json.JSONObject;

public abstract class TetrisMachine implements
        GameController,
        IGameOp,
        IJsonData{

    public abstract void onDraw(Canvas canvas, Rect frame,long frameTime);

    protected abstract void onBeginning();
    protected abstract boolean isNeedDeleteLines();

    protected abstract void onCountDown(long frameTime);
    protected abstract void onStartCountDown();
    protected abstract boolean isCountDownOver();

    protected abstract void generateNextTetris();

    /**
     * 是否碰底
     * @param dropDistance
     * @return
     */
    protected abstract boolean onDrop(float dropDistance);
    protected abstract float calculateDropDistance(long frameTime);
    protected abstract float calculateFastDropDistance(long frameTime);
    protected abstract void calculateRotate();
    protected abstract void calculateOffset(int moveStep, float dropDistance);

    protected abstract void onFillScene();
    protected abstract void onInitSize();
    protected abstract void onDeleteLine(long frameTime);
    protected abstract void onStartDeleteLine();
    protected abstract boolean isDeleteLineOver();
    protected abstract void onDeleteEnded();



    protected abstract boolean isGameOver();
    protected abstract void onRestart();
    protected abstract void onRestoreFromFile();
    protected abstract void onStop();

    public static final int STATE_NONE =       -2;
    public static final int STATE_BEGIN =       1;
    public static final int STATE_COUNTING =    2;
    public static final int STATE_NEXT_HOVER =  4;
    public static final int STATE_DROP =        5;
    public static final int STATE_DROP_HOVER =  6;
    public static final int STATE_FAST_DROP =   7;
    public static final int STATE_FILLING =     8;
    public static final int STATE_DELETE_LINE = 9;

    private boolean isAutoPause = true;
    private boolean isPause = false;
    private boolean isStop = true;
    private int currentState = STATE_NONE;
    private int needResumeState = STATE_NONE;
    private TetrisOp tetrisOp;
    private DropHoverTimer dropHoverTimer;
    private Timer nextHoverTimer;
    private GameLooper looper;
    public TetrisMachine(LoopCallback callback) {
        tetrisOp = new TetrisOp();
        dropHoverTimer = new DropHoverTimer();
        nextHoverTimer = new Timer(GameConfig.NEXT_HOVER_TIME);
        looper = new GameLooper(callback);
    }

    protected void loop(long frameTime){
        if (isPause())
            return;
        switch (currentState){
            case STATE_BEGIN:
                nextState();
                break;
            case STATE_COUNTING:
                if (isCountDownOver()){
                    nextState();
                }else{
                    onCountDown(frameTime);
                }
                break;
            case STATE_NEXT_HOVER:
                if (nextHoverTimer.isTimeOver()
                        || (!tetrisOp.isNop() && !tetrisOp.isNeedFastDrop())){
                    nextState();
                }else{
                    nextHoverTimer.pastTime(frameTime);
                }
                break;
            case STATE_DROP:
                if(tetrisOp.isNeedFastDrop()){
                    tetrisOp.resetFastDrop();
                    setState(STATE_FAST_DROP);
                }else{
                    float dropDistance;
                    if (tetrisOp.isNop()){
                        dropDistance = calculateDropDistance(frameTime);
                    }else{
                        dropDistance= calculateDropDistance(frameTime)*0.5f;
                        executeGameOp(dropDistance);
                    }

                    if (onDrop(dropDistance)) {
                        nextState();
                    }
                }
                break;
            case STATE_DROP_HOVER:
                if(dropHoverTimer.isTimeOver()){
                    nextState();
                }else{
                    if(!tetrisOp.isNeedFastDrop() && !tetrisOp.isNop()){
                        float dropDistance = calculateDropDistance(frameTime);
                        executeGameOp(dropDistance);
                        dropHoverTimer.restartTimer();
                        if(!onDrop(dropDistance)){
                            setState(STATE_DROP);
                        }
                    }else{
                        dropHoverTimer.pastTime(frameTime);
                    }
                }
                break;
            case STATE_FAST_DROP:
                float dropDistance = calculateFastDropDistance(frameTime);
                if (onDrop(dropDistance)) {
                    nextState();
                }
                break;
            case STATE_FILLING:
                onFillScene();
                nextState();
                break;
            case STATE_DELETE_LINE:
                if (isDeleteLineOver()){
                    onDeleteEnded();
                    nextState();
                }else{
                    onDeleteLine(frameTime);
                }
                break;
        }
    }

    protected void nextState(){
        switch (currentState){
            case STATE_BEGIN:
                setState(STATE_NEXT_HOVER);
                break;
            case STATE_COUNTING:
                if(needResumeState == STATE_NONE
                        || needResumeState == STATE_COUNTING ){
                    setState(STATE_NEXT_HOVER);
                }else{
                    setState(needResumeState);
                    needResumeState = STATE_NONE;
                }
                break;
            case STATE_NEXT_HOVER:
                setState(STATE_DROP);
                break;
            case STATE_DROP:
                setState(STATE_DROP_HOVER);
                break;
            case STATE_DROP_HOVER:
            case STATE_FAST_DROP:
                setState(STATE_FILLING);
                break;
            case STATE_FILLING:
                if (isNeedDeleteLines()){
                    setState(STATE_DELETE_LINE);
                }else{
                    setState(STATE_NEXT_HOVER);
                }
                break;
            case STATE_DELETE_LINE:
                setState(STATE_NEXT_HOVER);
                break;
        }
    }

    private void setState(int state){
        onLeaveState(currentState);
        currentState = state;
        onEnterState(currentState);
    }

    protected void onEnterState(int state){
        switch (state){
            case STATE_BEGIN:
                onBeginning();
                break;
            case STATE_COUNTING:
                if (isReadFromJson){
                    isReadFromJson = false;
                    onRestoreFromFile();
                }
                onStartCountDown();
                break;
            case STATE_NEXT_HOVER:
                tetrisOp.reset();
                generateNextTetris();
                if(isGameOver()){
                    isAutoPause = true;
                    looper.pauseLoop();
                }else{
                    nextHoverTimer.start();
                }
                break;
            case STATE_DROP:
                break;
            case STATE_DROP_HOVER:
                dropHoverTimer.startTimer();
                break;
            case STATE_FAST_DROP:
                break;
            case STATE_FILLING:
                break;
            case STATE_DELETE_LINE:
                onStartDeleteLine();
                break;
        }
    }


    protected void onLeaveState(int state){
        switch (state){
            case STATE_BEGIN:
                break;
            case STATE_COUNTING:
                break;
            case STATE_NEXT_HOVER:
                break;
            case STATE_DROP:
                break;
            case STATE_DROP_HOVER:
                break;
            case STATE_FAST_DROP:
                break;
            case STATE_FILLING:
                break;
            case STATE_DELETE_LINE:
                break;
        }
    }

    public boolean isPause(){
        return isAutoPause || isPause || isStop;
    }

    @Override
    public void pause() {
        isPause = true;
        if (currentState != STATE_COUNTING){
            needResumeState = currentState;
        }

        looper.pauseLoop();
    }

    protected void pauseWithoutHp(){
        isAutoPause = true;
        looper.pauseLoop();
    }

    @Override
    public void resume() {
        isPause = false;
        if(!isPause()){
            looper.resumeLoop();
            if(needResumeState != STATE_NONE) {
                setState(STATE_COUNTING);
            }
        }
    }

    @Override
    public void autoPause() {
        isAutoPause = true;
        if (currentState != STATE_COUNTING){
            needResumeState = currentState;
        }
        looper.pauseLoop();
    }

    @Override
    public void autoResume() {
        isAutoPause = false;
        if (!isPause()){
            looper.resumeLoop();
            if(needResumeState != STATE_NONE) {
                setState(STATE_COUNTING);
            }
        }
    }

    @Override
    public void start() {
        isStop = false;
        if (needResumeState != STATE_NONE){
            setState(STATE_COUNTING);
        }else {
            setState(STATE_BEGIN);
        }
        looper.startLoop();
    }

    @Override
    public void stop() {
        isStop = true;
        isPause = false;
        isAutoPause = true;
        needResumeState = STATE_NONE;
        currentState = STATE_BEGIN;
        tetrisOp.reset();
        looper.stopLoop();
        onStop();
    }

    @Override
    public void restart() {
        onRestart();
        isAutoPause = false;
        setState(STATE_BEGIN);
        looper.resumeLoop();
    }

    @Override
    public void rotate() {
        tetrisOp.rotate();
    }

    @Override
    public void moveLeft() {
        tetrisOp.moveLeft();
    }

    @Override
    public void moveRight() {
        tetrisOp.moveRight();
    }

    @Override
    public void fastDrop() {
        tetrisOp.fastDrop();
    }

    public int getCurrentState() {
        return currentState;
    }

    private void executeGameOp(float dropDistance) {
        if(tetrisOp.isNeedRotate()){
            calculateRotate();
            tetrisOp.resetNeedRotate();
        }

        int moveStep = tetrisOp.getMoveStep();
        if(moveStep != 0){
            calculateOffset(moveStep, dropDistance);
            tetrisOp.resetMoveStep();
        }
    }

    @Override
    public JSONObject writeToJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("currentState", currentState);
        json.put("needResumeState", needResumeState);
        json.put("nextHoverTimer", nextHoverTimer.writeToJson());
        return json;
    }

    private boolean isReadFromJson = false;
    @Override
    public void readFromJson(JSONObject json) throws Exception {
        currentState = json.getInt("currentState");
        needResumeState = json.getInt("needResumeState");
        nextHoverTimer.readFromJson(json.getJSONObject("nextHoverTimer"));
        isPause = false;
        isAutoPause = true;
        isStop = false;
        tetrisOp.reset();
        isReadFromJson = true;
    }
}
