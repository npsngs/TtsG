package com.grumpycat.tetrisgame;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.grumpycat.tetrisgame.tools.AppCache;

public abstract class PauseDialog extends Dialog implements View.OnClickListener{
    public PauseDialog(@NonNull Context context) {
        super(context, R.style.tetris_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pause);

        Context context  = getContext();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        window.setAttributes(lp);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView btn_continue = findViewById(R.id.btn_continue);
        TextView btn_save_exit = findViewById(R.id.btn_save_exit);
        TextView btn_exit = findViewById(R.id.btn_exit);

        Typeface tf = AppCache.getTypeface();
        tv_title.setTypeface(tf);
        btn_continue.setTypeface(tf);
        btn_save_exit.setTypeface(tf);
        btn_exit.setTypeface(tf);

        btn_continue.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_save_exit.setOnClickListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                continueGame();
                break;
            case R.id.btn_save_exit:
                saveAndExit();
                break;
            case R.id.btn_exit:
                exitGame();
                break;
        }
    }

    protected abstract void continueGame();
    protected abstract void saveAndExit();
    protected abstract void exitGame();

}
