package com.grumpycat.tetrisgame.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.widget.TextView;

import com.grumpycat.tetrisgame.R;

public class AppCache {
    private static Bitmap[] unitBitmaps;
    private static Bitmap shadow;
    private static float unitMargin;
    private static boolean hasLoaded = false;
    private static float volume;
    private static SharedPreferences sp;
    private static Typeface tf;
    private static float density;
    private static float scaledDensity;
    private static boolean isBtnsLayoutDefault;
    private static Context app;
    public static void preload(Context context){
        if (hasLoaded)
            return;
        app = context.getApplicationContext();
        Resources res = context.getResources();
        unitBitmaps = new Bitmap[8];
        unitBitmaps[0] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit11);
        unitBitmaps[1] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit12);
        unitBitmaps[2] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit13);
        unitBitmaps[3] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit14);
        unitBitmaps[4] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit15);
        unitBitmaps[5] = unitBitmaps[0];
        unitBitmaps[6] = unitBitmaps[1];
        unitBitmaps[7] = BitmapFactory.decodeResource(res, R.drawable.tetris_unit13);
        shadow = BitmapFactory.decodeResource(res, R.drawable.tetris_shadow);
        density = res.getDisplayMetrics().density;
        scaledDensity = res.getDisplayMetrics().scaledDensity;

        tf = Typeface.createFromAsset(context.getAssets(),"TAMA.TTF");

        sp = context.getSharedPreferences("records", Context.MODE_PRIVATE);
        volume = sp.getFloat("volume", 1.0f);
        isBtnsLayoutDefault = sp.getBoolean("btns_default", true);
        unitMargin = res.getDimension(R.dimen.unit_margin);
        hasLoaded = true;

    }

    public static Bitmap getShadow() {
        return shadow;
    }

    public static Bitmap[] getUnitBitmaps() {
        return unitBitmaps;
    }

    public static float getUnitMargin() {
        return unitMargin;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        AppCache.volume = volume;
        sp.edit().putFloat("volume", volume).apply();
    }

    public static boolean isBtnsLayoutDefault() {
        return isBtnsLayoutDefault;
    }

    public static void setBtnsLayoutDefault(boolean isBtnsLayoutDefault) {
        AppCache.isBtnsLayoutDefault = isBtnsLayoutDefault;
        sp.edit().putBoolean("btns_default", isBtnsLayoutDefault).apply();
    }

    public static void setTypeface(TextView tv) {
        tv.setTypeface(tf);
    }

    public static void setTypeface(Paint paint) {
        paint.setTypeface(tf);
    }

    public static void setTypeface(@IdRes int tvId, Activity activity) {
        TextView tv = activity.findViewById(tvId);
        setTypeface(tv);
    }

    public static float getDensity() {
        return density;
    }

    public static float getScaledDensity() {
        return scaledDensity;
    }

    public static Drawable loadDrawable(int resId){
        return app.getResources().getDrawable(resId);
    }

}
