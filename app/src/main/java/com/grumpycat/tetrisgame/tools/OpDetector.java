package com.grumpycat.tetrisgame.tools;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class OpDetector {
    private OnOpListener onOpListener;
    private final Handler mHandler;
    private boolean isPress = false;
    private OpController opController;

    public OpDetector(OnOpListener onOpListener) {
        this.onOpListener = onOpListener;
        mHandler = new MyHandler();
        opController = new SpeedUpController();
    }

    public void onTouchEvent(MotionEvent ev){
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (isPress)
                    return;
                if(onOpListener != null)
                    onOpListener.onDown();
                isPress = true;
                onOpDetected();
                opController.onStart();
                mHandler.sendEmptyMessageDelayed(0, opController.getInterval());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(onOpListener != null)
                    onOpListener.onUp();
                isPress = false;
                break;
        }
    }

    private void onOpDetected(){
        opController.onOp();
        if(onOpListener != null)
            onOpListener.onOp();
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(isPress){
                onOpDetected();
                mHandler.sendEmptyMessageDelayed(0, opController.getInterval());
            }
        }
    }


    public interface OnOpListener{
        void onDown();
        void onUp();
        void onOp();
    }


    private interface OpController{
        void onOp();
        void onStart();
        long getInterval();
    }

    private static class SpeedUpController implements OpController{
        private static final long MIN_INTERVAL = 30L;
        private static final long[] INTERVALS = {
                140L, 110L, 70L, MIN_INTERVAL
        };
        private long interval;
        private int count;
        @Override
        public void onOp() {
            count++;
            if(count < INTERVALS.length){
                interval = INTERVALS[count];
            }else {
                interval = MIN_INTERVAL;
            }
        }

        @Override
        public void onStart() {
            count = 0;
            interval = INTERVALS[0];
        }

        @Override
        public long getInterval() {
            return interval;
        }
    }
}
