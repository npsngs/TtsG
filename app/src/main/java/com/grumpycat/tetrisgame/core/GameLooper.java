package com.grumpycat.tetrisgame.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public class GameLooper implements ILooperController{
    private volatile boolean isStopped = true;
    private volatile boolean isPaused = true;
    private volatile Handler mHandler;
    private long frameDeltaTime;
    private LoopCallback callback;
    private long lastFrameTime = -1L;
    public GameLooper(LoopCallback callback) {
        this.callback = callback;
        frameDeltaTime = 1000/100;
    }

    @Override
    public void startLoop(){
        if(isStopped){
            isStopped = false;
            isPaused = true;
            HandlerThread thread = new HandlerThread("GAME LOOPER");
            thread.start();
            mHandler = new MyHandler(thread.getLooper());
            callback.onLoopStart(thread.getLooper());
        }
    }

    @Override
    public void stopLoop() {
        if (!isStopped){
            isStopped = true;
            mHandler.sendEmptyMessage(MSG_STOP);
        }
    }

    @Override
    public void pauseLoop() {
        if (isStopped)
            return;

        if (!isPaused){
            isPaused = true;
            mHandler.sendEmptyMessage(MSG_PAUSE);
        }
    }

    @Override
    public void resumeLoop() {
        if (isStopped)
            return;

        if (isPaused){
            isPaused = false;
            mHandler.sendEmptyMessage(MSG_RESUME);
        }
    }


    private static final int MSG_STOP =             1;
    private static final int MSG_SCHEDULE_NEXT =    2;
    private static final int MSG_PAUSE =            3;
    private static final int MSG_RESUME =           4;

    private class MyHandler extends Handler{
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_STOP:
                    mHandler.removeMessages(MSG_SCHEDULE_NEXT);
                    mHandler.getLooper().quit();
                    lastFrameTime = -1;
                    mHandler = null;
                    break;
                case MSG_SCHEDULE_NEXT:
                    loop();
                    break;
                case MSG_PAUSE:
                    mHandler.removeMessages(MSG_SCHEDULE_NEXT);
                    lastFrameTime = -1;
                    break;
                case MSG_RESUME:
                    loop();
                    break;
            }
        }
    }



    private void loop(){
        if (isStopped || isPaused) {
            return;
        }

        long startTime = SystemClock.uptimeMillis();
        callback.loop(lastFrameTime < 0?frameDeltaTime:startTime-lastFrameTime);
        long endTime = SystemClock.uptimeMillis();
        long pastTime = endTime - startTime;

        lastFrameTime = startTime;
        if(pastTime >= frameDeltaTime){
            mHandler.sendEmptyMessage(MSG_SCHEDULE_NEXT);
        }else{
            mHandler.sendEmptyMessageDelayed(MSG_SCHEDULE_NEXT, frameDeltaTime-pastTime);
        }
    }
}
