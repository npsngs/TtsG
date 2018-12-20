package com.grumpycat.tetrisgame.core;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.grumpycat.tetrisgame.GameConfig;
import com.grumpycat.tetrisgame.nodes.FPSNode;
import com.grumpycat.tetrisgame.nodes.SceneNode;
import com.grumpycat.tetrisgame.tools.CommonTools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


public class Director implements
        LoopCallback,
        UICallback,
        IGameOp,
        GameController{
    private static Director instance;

    public static Director getInstance(){
        if(instance == null){
            instance = new Director();
        }
        return instance;
    }

    private Director() {
        sceneNode = new SceneNode();
        tetrisMachine = new TetrisMachineImpl(sceneNode, this, this);
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder){
        holder = surfaceHolder;
        if(holder == null){
            return;
        }
        frame = surfaceHolder.getSurfaceFrame();
        sceneNode.initWithFrame(frame);
        tetrisMachine.onInitSize();
    }

    private SurfaceHolder holder;
    private SceneNode sceneNode;
    private FPSNode fpsNode = new FPSNode();
    private GameHandler gameHandler;
    private TetrisMachineImpl tetrisMachine;
    private Rect frame;
    private UIHandler uiHandler;


    @Override
    public void onLoopStart(Looper looper) {
        gameHandler = new GameHandler(
                looper,
                tetrisMachine,
                tetrisMachine);
    }


    public void loop(long frameTime){
        Canvas canvas = holder.lockCanvas();
        if(canvas == null)
            return;

        canvas.drawColor(0xff0c0c0c);
        tetrisMachine.onDraw(canvas, frame, frameTime);


        if(GameConfig.isShowFPS){
            fpsNode.setFrameTime(frameTime);
            fpsNode.draw(canvas, frame);
        }


        holder.unlockCanvasAndPost(canvas);
    }


    public void setUiHandler(UIHandler uiHandler) {
        this.uiHandler = uiHandler;
    }


    @Override
    public void pause(){
        gameHandler.pause();
    }

    @Override
    public void resume(){
        gameHandler.resume();
    }

    @Override
    public void autoPause() {
        gameHandler.autoPause();
    }

    @Override
    public void autoResume() {
        gameHandler.autoResume();
    }

    @Override
    public void start() {
        tetrisMachine.start();
    }

    @Override
    public void stop() {
        tetrisMachine.stop();
        gameHandler = null;
    }

    @Override
    public void restart() {
        gameHandler.restart();
    }


    public boolean onBackPressed(){
        return tetrisMachine.isPause();
    }


    @Override
    public void rotate() {
        gameHandler.rotate();
    }
    @Override
    public void moveLeft() {
        gameHandler.moveLeft();
    }
    @Override
    public void moveRight() {
        gameHandler.moveRight();
    }
    @Override
    public void fastDrop() {
        gameHandler.fastDrop();
    }

    @Override
    public void onNextMode(int mode) {
        if (uiHandler != null)
            uiHandler.onNextMode(mode);
    }

    @Override
    public void onLvlUp(int lvl) {
        if (uiHandler != null)
            uiHandler.onLvlUp(lvl);
    }

    @Override
    public void onScoreUp(int score) {
        if (uiHandler != null)
            uiHandler.onScoreUp(score);
    }

    @Override
    public void onLineUp(int line) {
        if (uiHandler != null)
            uiHandler.onLineUp(line);
    }

    @Override
    public void onGameOver() {
        if (uiHandler != null)
            uiHandler.onGameOver();
    }

    public void save(Context context){
        try{
            int versionCode = CommonTools.getVersionCode(context);
            JSONObject json = tetrisMachine.writeToJson();
            json.put("versionCode",versionCode);
            File f = new File(context.getCacheDir(), "game_save.json");
            if (!f.exists()) {
                f.createNewFile();
            }else if(!f.isFile()){
                f.delete();
                f.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
            Writer writer = new BufferedWriter(osw);
            writer.write(json.toString());
            writer.flush();

            writer.close();
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(Context context){
        File f = new File(context.getCacheDir(), "game_save.json");
        try{
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            Reader reader = new BufferedReader(isr);
            char[] buf = new char[1024];
            int ret;
            StringBuilder sb = new StringBuilder();
            while((ret = reader.read(buf, 0, 1024)) != -1){
                sb.append(buf, 0, ret);
            }

            reader.close();
            isr.close();
            fis.close();

            JSONObject json = new JSONObject(sb.toString());
            int versionCode = CommonTools.getVersionCode(context);
            int lastVersion = json.getInt("versionCode");
            if(versionCode == lastVersion){
                tetrisMachine.readFromJson(json);
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            f.delete();
        }
    }


    public boolean hasOldGame(Context context){
        try{
            File f = new File(context.getCacheDir(), "game_save.json");
            return f.exists() && f.isFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
