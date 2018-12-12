package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.ComboCounter;
import com.grumpycat.tetrisgame.core.IJsonData;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.CommonTools;

import org.json.JSONObject;

public class ComboNode implements GameNode ,IJsonData {
    private ComboCounter comboCounter;
    private Paint paint;
    private Paint hPaint;
    private RectF rectf;
    private float h;
    private final int HEIGHT = 4;
    private float padding;
    public ComboNode() {
        comboCounter = new ComboCounter();
        paint = new Paint();
        paint.setColor(0xfff3f3f3);
        paint.setAntiAlias(true);
        paint.setTextSize(CommonTools.sp2px(12f, AppCache.getScaledDensity()));
        paint.setTypeface(AppCache.getTypeface());

        float density = AppCache.getDensity();
        padding = density;

        hPaint = new Paint(paint);
        hPaint.setColor(0xffaf3723);
        hPaint.setStyle(Paint.Style.FILL);

        h = density*HEIGHT;
        rectf = new RectF(0, 0, 0, h);

    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if (comboCounter.isOnCombo()) {
            rectf.right = frame.width() * comboCounter.getRadio();
            canvas.drawRect(rectf, hPaint);
        }
        canvas.drawText(String.format("combo x%d", comboCounter.getCombo()),
                padding, h*3.5f, paint);
    }

    public void pastTime(long time) {
        comboCounter.pastTime(time);
    }

    public int hit(int base) {
        return comboCounter.hit(base);
    }

    public void reset() {
        comboCounter.reset();
    }

    @Override
    public JSONObject writeToJson() throws Exception {
        return comboCounter.writeToJson();
    }

    @Override
    public void readFromJson(JSONObject json) throws Exception {
        comboCounter.readFromJson(json);
    }
}
