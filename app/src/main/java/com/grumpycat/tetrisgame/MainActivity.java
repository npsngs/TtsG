package com.grumpycat.tetrisgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grumpycat.tetrisgame.core.Director;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.DBHelper;
import com.grumpycat.tetrisgame.tools.EnterAnimView;

public class MainActivity extends Activity implements View.OnClickListener{

    private TextView btn2, tv_score;
    private LinearLayout ll_btns;
    private EnterAnimView eav;
    private Animation animIn, animIn2, animOut,animOut2;
    private RelativeLayout rl_score;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_main);

        AppCache.preload(this);
        Typeface tf = AppCache.getTypeface();
        //设置字体
        TextView btn1 = findViewById(R.id.btn_start_new);
        btn2 = findViewById(R.id.btn_load_saved);
        TextView btn3 = findViewById(R.id.btn_others);
        tv_score = findViewById(R.id.tv_score);
        TextView tv_score_label = findViewById(R.id.tv_score_label);

        btn1.setTypeface(tf);
        btn2.setTypeface(tf);
        btn3.setTypeface(tf);
        tv_score.setTypeface(tf);
        tv_score_label.setTypeface(tf);

        rl_score = findViewById(R.id.rl_score);
        ll_btns = findViewById(R.id.ll_btns);
        eav = findViewById(R.id.eav);

        animIn = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_in);
        animOut = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_out);
        animIn2 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_in2);
        animOut2 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_out2);

        eav.setOnAnimLister(new EnterAnimView.OnAnimLister() {
            @Override
            public void onCloseFinish() {
                finish();
            }

            @Override
            public void onStartOpen() {
                ll_btns.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_btns.setVisibility(View.VISIBLE);
                        ll_btns.startAnimation(animIn);
                    }
                },300);
            }

            @Override
            public void onStartClose() {
                ll_btns.startAnimation(animOut);
                rl_score.startAnimation(animOut2);
            }
        });

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        DBHelper.init(getApplication());
    }

    private void loadScore(){
        SharedPreferences sp = getSharedPreferences("records", MODE_PRIVATE);
        int bestHistory = sp.getInt("bestHistory", 0);
        if(bestHistory > 0){
            rl_score.setVisibility(View.VISIBLE);
            tv_score.setText(bestHistory+"");
            rl_score.startAnimation(animIn2);
        }
    }

    @Override
    public void onBackPressed() {
        eav.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn2.setEnabled(Director.getInstance().hasOldGame(this));
        ll_btns.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadScore();
            }
        },1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_new:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                //StatService.onEvent(this, "start_game", "NewGame");
                break;
            case R.id.btn_load_saved:
                loadSave();
                //StatService.onEvent(this, "start_game", "SavedGame");
                break;
            case R.id.btn_others:
                showOtherDialog();
                //StatService.onEvent(this, "others", "others");
                break;
        }
    }

    private void showOtherDialog(){
        OthersDialog dialog = new OthersDialog(this);
        dialog.setCancelable(true);
        dialog.show();
    }


    private void loadSave(){
        new Thread(){
            @Override
            public void run() {
                Director.getInstance().load(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }.start();
    }

}
