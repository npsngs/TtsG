package com.grumpycat.tetrisgame.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public class GameHandler implements IGameOp, GameController {
    private Handler handler;
    private static final int MSG_ROTATE =       1;
    private static final int MSG_MOVE_L =       2;
    private static final int MSG_MOVE_R =       3;
    private static final int MSG_DROP =         4;
    private static final int MSG_RESUME =       5;
    private static final int MSG_PAUSE =        6;
    private static final int MSG_AUTO_PAUSE =   7;
    private static final int MSG_AUTO_RESUME =  8;
    private static final int MSG_START =        9;
    private static final int MSG_STOP =         10;
    private static final int MSG_RESTART =      11;
    private IGameOp gameOp;
    private GameController gameController;
    public GameHandler(Looper looper, IGameOp op, GameController controller) {
        this.gameOp = op;
        this.gameController = controller;
        handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_ROTATE:
                        gameOp.rotate();
                        break;
                    case MSG_MOVE_L:
                        gameOp.moveLeft();
                        break;
                    case MSG_MOVE_R:
                        gameOp.moveRight();
                        break;
                    case MSG_DROP:
                        gameOp.fastDrop();
                        break;
                    case MSG_RESUME:
                        gameController.resume();
                        break;
                    case MSG_PAUSE:
                        gameController.pause();
                        break;
                    case MSG_AUTO_PAUSE:
                        gameController.autoPause();
                        break;
                    case MSG_AUTO_RESUME:
                        gameController.autoResume();
                        break;
                    case MSG_START:
                        gameController.start();
                        break;
                    case MSG_STOP:
                        gameController.stop();
                        break;
                    case MSG_RESTART:
                        gameController.restart();
                        break;
                }
            }
        };
    }

    public void rotate(){
        handler.sendEmptyMessage(MSG_ROTATE);
    }

    @Override
    public void moveLeft() {
        handler.sendEmptyMessage(MSG_MOVE_L);
    }

    @Override
    public void moveRight() {
        handler.sendEmptyMessage(MSG_MOVE_R);
    }

    @Override
    public void fastDrop() {
        handler.sendEmptyMessage(MSG_DROP);
    }

    @Override
    public void pause() {
        handler.sendEmptyMessage(MSG_PAUSE);
    }

    @Override
    public void resume() {
        handler.sendEmptyMessage(MSG_RESUME);
    }

    @Override
    public void autoPause() {
        handler.sendEmptyMessage(MSG_AUTO_PAUSE);
    }

    @Override
    public void autoResume() {
        handler.sendEmptyMessage(MSG_AUTO_RESUME);
    }

    @Override
    public void start() {
        handler.sendEmptyMessage(MSG_START);
    }

    @Override
    public void stop() {
        handler.sendEmptyMessage(MSG_STOP);
    }

    @Override
    public void restart() {
        handler.sendEmptyMessage(MSG_RESTART);
    }
}
