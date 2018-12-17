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
import com.grumpycat.tetrisgame.tools.SoundManager;

public abstract class GameOverDialog extends Dialog implements View.OnClickListener{
    private int highestScore, score;
    public GameOverDialog(@NonNull Context context,int highestScore, int score) {
        super(context, R.style.tetris_dialog);
        this.highestScore = highestScore;
        this.score = score;
    }

    TextView tv_title,tv_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gameover);

        Context context  = getContext();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        window.setAttributes(lp);


        tv_title = findViewById(R.id.tv_title);
        TextView btn_restart = findViewById(R.id.btn_restart);
        tv_score = findViewById(R.id.tv_score);
        TextView tv_label_score = findViewById(R.id.tv_label_score);
        TextView btn_exit = findViewById(R.id.btn_exit);


        AppCache.setTypeface(tv_title);
        AppCache.setTypeface(btn_restart);
        AppCache.setTypeface(tv_score);
        AppCache.setTypeface(btn_exit);
        AppCache.setTypeface(tv_title);
        AppCache.setTypeface(tv_label_score);

        btn_restart.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setScore(highestScore, score);
    }

    public void setScore(int highestScore, int score) {
        this.highestScore = highestScore;
        this.score = score;
        if (tv_title == null){
            return;
        }
        if(highestScore <= score){
            SoundManager.playCongratulations();
            tv_title.setText(R.string.highest_score);
        }else{
            SoundManager.playGameover();
            float p = score*1f/highestScore;
            if(p > 0.65f){
                tv_title.setText(R.string.highest_almost);
            }else{
                tv_title.setText(R.string.whack);
            }
        }
        tv_score.setText(score+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_restart:
                restartGame();
                break;
            case R.id.btn_exit:
                exitGame();
                break;
        }
    }

    protected abstract void restartGame();
    protected abstract void exitGame();
}
