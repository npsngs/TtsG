package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.grumpycat.tetrisgame.GameConfig;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.CommonTools;

public class InfoNode implements GameNode {
    private final long ANIM_TIME = 300L;
    private Paint paint;
    private boolean isShow = false;
    private String str;
    private float y;
    private long time;
    private long MAX_TIME;
    private float MAX_DY;
    private float fraction;
    private float textSize;
    public InfoNode() {
        MAX_TIME = GameConfig.SCORE_SHOW_TIME+ANIM_TIME;
        paint = new Paint();
        paint.setColor(0xfffefefe);
        paint.setAntiAlias(true);
        MAX_DY = CommonTools.dp2px(30, AppCache.getDensity());
        textSize = CommonTools.sp2px(36f, AppCache.getScaledDensity());
        paint.setTextSize(textSize);
        AppCache.setTypeface(paint);
    }

    public void show(int addScore, int lines, float y){
        time = 0;
        isShow = true;
        str = "+"+addScore;
        this.y = y;
        fraction = 0f;
        switch (lines){
            case 1:
                paint.setColor(0xfffefefe);
                paint.setTextSize(textSize);
                break;
            case 2:
                paint.setColor(0xfffefefe);
                paint.setTextSize(textSize*1.2f);
                break;
            case 3:
                paint.setColor(0xfffefefe);
                paint.setTextSize(textSize*1.5f);
                break;
            case 4:
                paint.setColor(0xffffb400);
                paint.setTextSize(textSize*2f);
                break;
        }
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if (isShow){
            canvas.save();
            canvas.translate(0, -fraction*MAX_DY);
            paint.setAlpha((int) ((1f-fraction)*255));
            canvas.drawText(str, frame.centerX(), y, paint);
            canvas.restore();
        }
    }

    public void update(long frameTime){
        time += frameTime;
        if (time < GameConfig.SCORE_SHOW_TIME){
            fraction = 0f;
        }else if(time <= MAX_TIME){
            fraction = (time-GameConfig.SCORE_SHOW_TIME)*1f/ANIM_TIME;
        }else{
            fraction = 1f;
            isShow = false;
        }
    }

}
