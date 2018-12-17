package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.CommonTools;

public class CountDownNode implements GameNode {
    private boolean isOver = false;
    private final int MAX_COUNT = 3;
    private final String[] STR = {"1","2","3"};
    private final long COUNT_DOWN_DURATION = 800;
    private long startTime;
    private int count;
    private Paint paint;
    public boolean isOver(){
        return isOver;
    }
    private float offset;
    public CountDownNode() {
        paint = new Paint();
        paint.setColor(0xffffef8f);
        paint.setAntiAlias(true);
        paint.setTextSize(CommonTools.sp2px(70f, AppCache.getScaledDensity()));
        AppCache.setTypeface(paint);
        offset = paint.measureText("2")/3;
    }

    public void start(){
        isOver = false;
        startTime = 0L;
        count = MAX_COUNT;
    }

    public void pastFrameTime(long frameTime){
        startTime += frameTime;
        if(startTime > COUNT_DOWN_DURATION){
            startTime -= COUNT_DOWN_DURATION;
            count--;
            if(count <=0 )
                isOver = true;
        }
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if (!isOver && count > 0){
            canvas.drawText(STR[count-1],frame.centerX()-offset,frame.centerY(), paint);
        }
    }
}
