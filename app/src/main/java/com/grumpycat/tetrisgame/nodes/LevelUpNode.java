package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.grumpycat.tetrisgame.GameConfig;
import com.grumpycat.tetrisgame.R;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.CommonTools;

public class LevelUpNode implements GameNode {
    private String str;
    private Drawable d;
    private Paint paint;
    private float offsetText;
    private boolean isInit = false;
    private boolean isEnded = true;
    private boolean isBgEnded = true;
    private long time;
    private float dy;
    private int startY, endY;
    private long MAX_TEXT_TIME;
    private long TEXT_FADING_TIME = 300L;
    private long TEXT_START_FADING_TIME;
    private int alpha = 255;
    public void startLevelUp(int lv){
        str = "level "+lv;
        isEnded = false;
        isBgEnded = false;
        time = 0L;
        alpha = 255;
        dy = 0.0f;
    }

    public LevelUpNode() {
        MAX_TEXT_TIME = GameConfig.LEVEL_UP_TIME*5/4;
        TEXT_START_FADING_TIME = MAX_TEXT_TIME - TEXT_FADING_TIME;
        d = AppCache.loadDrawable(R.drawable.ic_lvl_up_bg);
        paint = new Paint();
        paint.setColor(0xffffce00);
        paint.setAntiAlias(true);
        paint.setTextSize(CommonTools.sp2px(40f, AppCache.getScaledDensity()));
        paint.setTypeface(AppCache.getTypeface());
        offsetText = paint.measureText("level up")/2;
    }

    private void init(Rect frame){
        d.setBounds(0,0, frame.width(), d.getIntrinsicHeight());
        startY = frame.height();
        endY = -d.getIntrinsicHeight();
        isInit = true;
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if (isEnded)
            return;

        if (!isInit)
            init(frame);

        if (!isBgEnded) {
            canvas.save();
            canvas.translate(0f, dy);
            d.draw(canvas);
            canvas.restore();
        }

        paint.setAlpha(alpha);
        canvas.drawText(str,frame.centerX()-offsetText,frame.centerY(), paint);
    }

    public void update(long frameTime) {
        time += frameTime;
        if (!isBgEnded){
            if (time <= GameConfig.LEVEL_UP_TIME) {
                dy = (time * 1f / GameConfig.LEVEL_UP_TIME) * (endY - startY) + startY;
            } else {
                dy = endY;
                isBgEnded = true;
            }
        }

        if (!isEnded){
            if(time <= TEXT_START_FADING_TIME){
                alpha = 255;
            }else if(time <= MAX_TEXT_TIME){
                alpha = (int) ((1 - ((time-TEXT_START_FADING_TIME)*1f/TEXT_FADING_TIME))*255);
            }else{
                alpha = 0;
                isEnded = true;
            }
        }
    }

    public boolean isEnded() {
        return isEnded;
    }
}
