package com.grumpycat.tetrisgame.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class UIHandler implements UICallback{
    private Handler handler;
    private static final int MSG_NEXT_MODE =    1;
    private static final int MSG_LVL_UP =       2;
    private static final int MSG_SCORE_UP =     3;
    private static final int MSG_LINE_UP =      4;
    private static final int MSG_GAMEOVER=      5;

    private UICallback callback;
    public UIHandler(UICallback uiCallback) {
        this.callback = uiCallback;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_NEXT_MODE:
                        callback.onNextMode(msg.arg1);
                        break;
                    case MSG_LVL_UP:
                        callback.onLvlUp(msg.arg1);
                        break;
                    case MSG_SCORE_UP:
                        callback.onScoreUp(msg.arg1);
                        break;
                    case MSG_LINE_UP:
                        callback.onLineUp(msg.arg1);
                        break;
                    case MSG_GAMEOVER:
                        callback.onGameOver();
                        break;
                }
            }
        };
    }

    @Override
    public void onNextMode(int mode) {
        handler.obtainMessage(MSG_NEXT_MODE, mode, 0).sendToTarget();
    }

    @Override
    public void onLvlUp(int lvl) {
        handler.obtainMessage(MSG_LVL_UP, lvl, 0).sendToTarget();
    }

    @Override
    public void onScoreUp(int score) {
        handler.obtainMessage(MSG_SCORE_UP, score, 0).sendToTarget();
    }

    @Override
    public void onLineUp(int line) {
        handler.obtainMessage(MSG_LINE_UP, line, 0).sendToTarget();
    }

    @Override
    public void onGameOver() {
        handler.sendEmptyMessage(MSG_GAMEOVER);
    }

}
