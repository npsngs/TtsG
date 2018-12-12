package com.grumpycat.tetrisgame.core;


import android.graphics.Canvas;
import android.graphics.Rect;

import com.grumpycat.tetrisgame.nodes.ComboNode;
import com.grumpycat.tetrisgame.nodes.CountDownNode;
import com.grumpycat.tetrisgame.nodes.InfoNode;
import com.grumpycat.tetrisgame.nodes.LevelUpNode;
import com.grumpycat.tetrisgame.nodes.SceneNode;
import com.grumpycat.tetrisgame.nodes.TetrisNode;
import com.grumpycat.tetrisgame.tools.SceneShaker;
import com.grumpycat.tetrisgame.tools.SoundManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TetrisMachineImpl extends TetrisMachine {
    private SceneNode sceneNode;
    private TetrisNode tetrisNode;
    private LevelUpNode levelUpNode;
    private InfoNode infoNode;
    private ComboNode comboNode;
    private ModeGenerator modeGenerator;
    private GameCalculator gameCalculator;
    private TetrisGenerator tetrisGenerator;
    private SceneShaker sceneShaker;
    private CountDownNode countDownNode;
    private UICallback uiCallback;
    private int lv = 1;
    private float unitSize;
    private int score = 0;
    private int lines = 0;
    private int count = 0;
    private int[] deleteLines = new int[4];
    private int deleteLineNum = 0;
    private int deleteMaxLine = 0;

    public TetrisMachineImpl(SceneNode sceneNode, final UICallback uiCallback, LoopCallback loopCallback) {
        super(loopCallback);
        this.sceneNode = sceneNode;
        this.uiCallback = uiCallback;
        modeGenerator = new ModeGenerator();
        tetrisGenerator = new TetrisGenerator();
        countDownNode = new CountDownNode();
        sceneShaker = new SceneShaker();
        comboNode = new ComboNode();
        gameCalculator = new GameCalculator();
    }

    @Override
    public void onDraw(Canvas canvas, Rect frame, long frameTime){
        loop(frameTime);
        gameCalculator.pastTime(frameTime);
        if (sceneShaker.isShaking()){
            canvas.save();
            float fraction = sceneShaker.shakeScene(frameTime);
            float dy = sceneNode.getUnitMargin()*fraction*0.2f;
            canvas.translate(0f, dy);
            sceneNode.draw(canvas, frame);
            canvas.restore();
        }else{
            sceneNode.draw(canvas, frame);
        }


        if(tetrisNode != null){
            tetrisNode.draw(canvas, frame);
        }

        if(!countDownNode.isOver()){
            countDownNode.draw(canvas,frame);
        }

        if(levelUpNode != null && !levelUpNode.isEnded()){
            levelUpNode.update(frameTime);
            levelUpNode.draw(canvas, frame);
        }

        if (infoNode != null && infoNode.isShow()){
            infoNode.update(frameTime);
            infoNode.draw(canvas, frame);
        }

        if (getCurrentState() != STATE_COUNTING) {
            comboNode.pastTime(frameTime);
        }
        comboNode.draw(canvas, frame);
    }

    @Override
    protected void onBeginning() {
        lv = 1;
        lines = 0;
        score = 0;
        count = 0;
        gameCalculator.reset();
        gameCalculator.onLevelUp(lv);
        uiCallback.onScoreUp(score);
        uiCallback.onLvlUp(lv);
        uiCallback.onLineUp(lines);
        comboNode.reset();
        sceneNode.reset();
    }


    @Override
    public boolean isGameOver() {
        boolean isGameOver = checkGameOver();
        if (isGameOver){
            uiCallback.onGameOver();
        }
        return isGameOver;
    }

    @Override
    protected void onRestart() {
        comboNode.reset();
        sceneNode.reset();
    }

    @Override
    protected void onRestoreFromFile() {
        if (tetrisNode != null){
            tetrisNode.initWithSize(sceneNode.getUnitSize(),
                    sceneNode.getUnitMargin(),
                    sceneNode.getRealFrame());
        }
        uiCallback.onLvlUp(lv);
        uiCallback.onScoreUp(score);
        uiCallback.onLineUp(lines);
        uiCallback.onNextMode(modeGenerator.getNextMode());
        modeGenerator.next();
    }

    @Override
    protected void onStop() {
        gameCalculator.reset();
        sceneNode.reset();
        comboNode.reset();
        tetrisNode = null;
    }

    private boolean checkGameOver(){
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        int offsetX = tetrisNode.getOffsetX();
        int offsetY = tetrisNode.getOffsetY();

        List<int[]> indexs = tetrisMatrix.getValidUnits();
        for(int[] index:indexs) {
            int x = index[0];
            int y = index[1];
            int sx = offsetX + x;
            int sy = offsetY + y;
            if(sceneMatrix.get(sx, sy) != UnitMatrix.NULL){
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isNeedDeleteLines() {
        return deleteLineNum > 0;
    }

    @Override
    protected void onCountDown(long frameTime) {
        countDownNode.pastFrameTime(frameTime);
    }

    @Override
    protected void onStartCountDown() {
        countDownNode.start();
    }

    @Override
    protected boolean isCountDownOver() {
        return countDownNode.isOver();
    }

    @Override
    protected void generateNextTetris() {
        int mode = modeGenerator.getCurMode();
        uiCallback.onNextMode(modeGenerator.next());
        tetrisNode = tetrisGenerator.getTetrisNode(mode, sceneNode);
        tetrisNode.reset();
        calculateShadowY();
        count++;
    }


    @Override
    protected boolean onDrop(float dropDistance) {
        int offsetY = tetrisNode.getOffsetY();
        float dropY = tetrisNode.getDropY();
        int shadowY = tetrisNode.getShadowY();
        float afterOffsetY = offsetY + (dropY+dropDistance)/unitSize;
        if(afterOffsetY >= shadowY){
            tetrisNode.enterShadowShot();
            return true;
        }else{
            tetrisNode.drop(dropDistance);
            return false;
        }
    }

    @Override
    protected float calculateDropDistance(long frameTime) {
        return frameTime * gameCalculator.speed;
    }

    @Override
    protected float calculateFastDropDistance(long frameTime){
        return frameTime * gameCalculator.fastSpeed;
    }

    @Override
    protected void calculateRotate() {
        if(tetrisNode.getDirectionDimen() == 1)
            return;

        if(!tryRotate()) {
            tetrisNode.backwardRotate();
        }else{
            calculateShadowY();
            SoundManager.playClick();
        }
    }


    public void calculateShadowY(){
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        List<int[]> indexs = tetrisMatrix.getValidUnits();
        int offsetX = tetrisNode.getOffsetX();
        int offsetY = tetrisNode.getOffsetY()+1;
        if(tetrisNode.getDropY() > 0){
            offsetY += 1;
        }
        boolean isTouchBottom = false;
        do {
            for (int[] index : indexs) {
                int x = index[0];
                int y = index[1];
                int sx = offsetX + x;
                int sy = offsetY + y;
                int value = sceneMatrix.get(sx, sy);
                if(value != UnitMatrix.NULL){
                    tetrisNode.setShadowY(offsetY -1);
                    isTouchBottom = true;
                    break;
                }
            }
            offsetY++;
        }while (!isTouchBottom);
    }


    private boolean tryRotate(){
        tetrisNode.rotate();
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        int offsetX = tetrisNode.getOffsetX();
        int offsetY = tetrisNode.getOffsetY();
        if(tetrisNode.getDropY() > 0){
            offsetY++;
        }
        List<int[]> indexs = tetrisMatrix.getValidUnits();
        int[] revise = new int[2];
        boolean hasCollide = false;
        for(int[] index:indexs) {
            int x = index[0];
            int y = index[1];
            int sx = offsetX + x;
            int sy = offsetY + y;
            int value = sceneMatrix.get(sx, sy);
            if (value != UnitMatrix.NULL) {
                hasCollide = true;
                boolean isRevised = tetrisNode.reviseWhenCollide(x, y, revise);
                if (isRevised && reviseRotate(revise)) {
                    return true;
                }
            }
        }
        return !hasCollide;
    }

    private boolean reviseRotate(int[] offsets){
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        List<int[]> indexs = tetrisMatrix.getValidUnits();
        int offsetX = tetrisNode.getOffsetX();
        int offsetY = tetrisNode.getOffsetY();
        if(tetrisNode.getDropY() > 0){
            offsetY++;
        }
        for(int[] index:indexs) {
            int x = index[0];
            int y = index[1];
            int sx = offsetX + x + offsets[0];
            int sy = offsetY + y + offsets[1];
            int value = sceneMatrix.get(sx, sy);
            if (value != UnitMatrix.NULL) {
                return false;
            }
        }

        tetrisNode.offset(offsets[0], offsets[1]);
        return true;
    }

    @Override
    protected void calculateOffset(int moveStep, float dropDistance) {
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        int offsetX = tetrisNode.getOffsetX() + moveStep;
        int offsetY = tetrisNode.getOffsetY();
        if(tetrisNode.getDropY() > 0){
            offsetY++;
        }

        List<int[]> indexs = tetrisMatrix.getValidUnits();
        for(int[] index:indexs) {
            int sx = offsetX + index[0];
            int sy = offsetY + index[1];
            if(sceneMatrix.get(sx, sy) != UnitMatrix.NULL){
                return;
            }
        }
        tetrisNode.offsetLOrR(moveStep);
        calculateShadowY();
        SoundManager.playClick();
    }

    @Override
    protected void onFillScene() {
        UnitMatrix sceneMatrix = sceneNode.getUnitMatrix();
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        int offsetX = tetrisNode.getOffsetX();
        int offsetY = tetrisNode.getOffsetY();
        List<int[]> ids = tetrisMatrix.getValidUnits();
        for (int[] index : ids) {
            int x = index[0];
            int y = index[1];
            sceneMatrix.set(x + offsetX, y + offsetY, tetrisMatrix.get(x, y));
        }


        deleteLineNum = 0;
        deleteMaxLine = -1;
        int tetrisRow = tetrisMatrix.getRow();
        int len = deleteLines.length;
        for(int i =0;i<len;i++){
            if(i >= tetrisRow) {
                deleteLines[i] = -1;
                continue;
            }

            if(tetrisMatrix.isEmptyLine(i)){
                deleteLines[i] = -1;
            }else{
                int line = i+offsetY;
                if(sceneMatrix.isFinishLine(line)){
                    deleteLines[i] = line;
                    if (line > deleteMaxLine)
                        deleteMaxLine = line;
                    deleteLineNum++;
                }else{
                    deleteLines[i] = -1;
                }
            }
        }
        if (deleteLineNum > 0)
            Arrays.sort(deleteLines);
    }

    @Override
    protected void onInitSize() {
        unitSize = sceneNode.getUnitSize();
        gameCalculator.setUnitSize(unitSize);
        gameCalculator.onLevelUp(lv);
    }

    @Override
    protected void onDeleteLine(long frameTime) {
        sceneNode.deleteLines(frameTime, deleteLines);
    }

    @Override
    protected void onStartDeleteLine() {
        SoundManager.playClearLine(deleteLineNum);
        sceneNode.startDelete();
    }

    @Override
    protected boolean isDeleteLineOver() {
        return !sceneNode.isOnDelete();
    }

    @Override
    protected void onDeleteEnded() {
        calculateScore();
        sceneShaker.startShake();
    }

    private void calculateScore(){
        this.lines += deleteLineNum;
        uiCallback.onLineUp(this.lines);
        calculateLvlUp(this.lines);

        int combo = comboNode.hit(deleteLineNum*deleteLineNum);
        int addScore = gameCalculator.calculateScore(deleteLineNum, combo);
        this.score += addScore;
        if(infoNode == null){
            infoNode = new InfoNode();
        }
        infoNode.show(addScore, sceneNode.getLineY(deleteMaxLine));
        uiCallback.onScoreUp(this.score);
    }

    private void calculateLvlUp(int lines){
        int newLvl = gameCalculator.calculateLvl(lines);
        if(newLvl > lv){
            lv = newLvl;
            gameCalculator.onLevelUp(lv);
            uiCallback.onLvlUp(lv);
            if (levelUpNode == null){
                levelUpNode = new LevelUpNode();
            }
            levelUpNode.startLevelUp(lv);
            SoundManager.playLevelUp();
        }
    }


    @Override
    public JSONObject writeToJson() throws Exception {
        JSONObject json = super.writeToJson();

        JSONArray deleteArray = new JSONArray();
        for(int line:deleteLines){
            deleteArray.put(line);
        }

        json
                .put("lv", lv)
                .put("lines", lines)
                .put("score", score)
                .put("count", count)
                .put("pauseCount", 1)
                .put("scene", sceneNode.writeToJson())
                .put("modeGen", modeGenerator.writeToJson())
                .put("deleteLineNum",deleteLineNum)
                .put("deleteLines", deleteArray)
                .put("combo", comboNode.writeToJson());
        if (tetrisNode != null){
            json.put("tetris", tetrisNode.writeToJson());
        }

        return json;
    }

    @Override
    public void readFromJson(JSONObject json) throws Exception {
        super.readFromJson(json);
        lv = json.getInt("lv");
        lines = json.getInt("lines");
        score = json.getInt("score");
        count = json.getInt("count");
        deleteLineNum = json.getInt("deleteLineNum");
        JSONArray deleteArray = json.getJSONArray("deleteLines");
        for(int i=0;i<4;i++){
            deleteLines[i] = deleteArray.getInt(i);
        }
        sceneNode.readFromJson(json.getJSONObject("scene"));
        modeGenerator.readFromJson(json.getJSONObject("modeGen"));

        if (json.has("tetris")){
            JSONObject tetrisJson = json.getJSONObject("tetris");
            tetrisNode = tetrisGenerator.getTetrisNode(modeGenerator.getCurMode(), sceneNode);
            tetrisNode.readFromJson(tetrisJson);
        }

        comboNode.readFromJson(json.getJSONObject("combo"));
    }

    @Override
    protected void onLeaveState(int state) {
        super.onLeaveState(state);
        switch (state){
            case STATE_FAST_DROP:
                SoundManager.playFastDrop();
                break;
        }
    }
}
