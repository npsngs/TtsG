package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FPSNode implements GameNode {
    private Paint textPaint;
    private long frameTime;
    private int maxSkipFrame = 30;
    private int skipFrame = 0;
    private long pastTime = 0;
    private String frameStr = null;
    public FPSNode() {
        textPaint = new Paint();
        textPaint.setColor(0xff00ff00);
        textPaint.setTextSize(18f);
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if(frameStr == null){
            float fps = 1000f/frameTime;
            frameStr = String.format("FPS:%.01f",fps);
        }else{
            if(skipFrame < maxSkipFrame){
                skipFrame++;
                pastTime += frameTime;
            }else{
                float fps = 1000f*maxSkipFrame/pastTime;
                frameStr = String.format("FPS:%.01f",fps);
                skipFrame = 0;
                pastTime = 0;
            }
        }



        canvas.drawText(frameStr, 10,30, textPaint);
    }
}
