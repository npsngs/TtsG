package com.grumpycat.tetrisgame;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.VolumeView;

public class OthersDialog extends Dialog{
    private VolumeView vv;
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
            }
        });
        Typeface tf = AppCache.getTypeface();
        tv_email.setTypeface(tf);
        tv_studio.setTypeface(tf);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);


    }
}
