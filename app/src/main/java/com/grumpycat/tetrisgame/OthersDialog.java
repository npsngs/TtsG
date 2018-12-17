package com.grumpycat.tetrisgame;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.EventLog;
import com.grumpycat.tetrisgame.tools.VolumeView;

public class OthersDialog extends Dialog{
    private VolumeView vv;
    private View btn_layout1,btn_layout2;
    private boolean hasChangeVolume = false;
    public OthersDialog(@NonNull Context context) {
        super(context, R.style.tetris_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_others);

        Context context  = getContext();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        window.setAttributes(lp);

        TextView tv_studio = findViewById(R.id.tv_studio);
        TextView tv_email = findViewById(R.id.tv_email);
        vv = findViewById(R.id.vv);
        vv.setProgress(AppCache.getVolume());
        vv.setOnProgressChangeListener(new VolumeView.OnProgressChangeListener() {
            @Override
            public void onProgressChange(float progress) {
                AppCache.setVolume(progress);
                hasChangeVolume = true;
            }
        });

        AppCache.setTypeface(tv_email);
        AppCache.setTypeface(tv_studio);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        btn_layout1 = findViewById(R.id.btn_layout1);
        btn_layout2 = findViewById(R.id.btn_layout2);
        if(AppCache.isBtnsLayoutDefault()){
            btn_layout1.setAlpha(1.0f);
            btn_layout2.setAlpha(0.3f);
        }else{
            btn_layout1.setAlpha(0.3f);
            btn_layout2.setAlpha(1.0f);
        }

        TextView tv_toggle_btns = findViewById(R.id.tv_toggle_btn);
        AppCache.setTypeface(tv_toggle_btns);
        findViewById(R.id.ll_toggle_btns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AppCache.isBtnsLayoutDefault()){
                    AppCache.setBtnsLayoutDefault(true);
                    btn_layout1.setAlpha(1.0f);
                    btn_layout2.setAlpha(0.3f);
                    EventLog.logClick("toggle default btns");
                }else{
                    AppCache.setBtnsLayoutDefault(false);
                    btn_layout1.setAlpha(0.3f);
                    btn_layout2.setAlpha(1.0f);
                    EventLog.logClick("toggle linear btns");
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(hasChangeVolume){
            EventLog.logClick("change volume");
        }
    }
}
