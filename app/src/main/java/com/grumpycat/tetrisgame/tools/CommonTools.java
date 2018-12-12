package com.grumpycat.tetrisgame.tools;

import android.content.Context;

public class CommonTools {
    public static int dp2px(float dp, float density){
        return  (int) (dp*density+0.5f);
    }

    public static int sp2px(float sp, float scaleDensity){
        return  (int) (sp*scaleDensity+0.5f);
    }


    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verName;
    }
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
