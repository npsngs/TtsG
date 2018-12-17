package com.grumpycat.tetrisgame.tools;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EventLog {
    private static FirebaseAnalytics fba;
    public static void init(Activity activity){
        fba = FirebaseAnalytics.getInstance(activity);
    }

    public static void log(String name, String value){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, value);
        fba.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
