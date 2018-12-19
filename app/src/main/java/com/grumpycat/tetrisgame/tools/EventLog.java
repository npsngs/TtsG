package com.grumpycat.tetrisgame.tools;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EventLog {
    private static FirebaseAnalytics fba;
    public static void init(Activity activity){
        fba = FirebaseAnalytics.getInstance(activity);
    }

    public static void logGameOver(int lvl, int line, int score){
        Bundle bundle = new Bundle();
        bundle.putInt("level", lvl);
        bundle.putInt("line", line);
        bundle.putInt("score", score);
        fba.logEvent("game_info", bundle);
    }

    public static void logPageJump(String name){
        Bundle bundle = new Bundle();
        bundle.putString("page_name", name);
        fba.logEvent("gt_page_jump", bundle);
    }

    public static void logClick(String where){
        Bundle bundle = new Bundle();
        bundle.putString("click_where", where);
        fba.logEvent("gt_click_action", bundle);
    }
}
