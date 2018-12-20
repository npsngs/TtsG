package com.grumpycat.tetrisgame;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.grumpycat.tetrisgame.core.Director;
import com.grumpycat.tetrisgame.core.UICallback;
import com.grumpycat.tetrisgame.core.UIHandler;
import com.grumpycat.tetrisgame.tools.AppCache;
import com.grumpycat.tetrisgame.tools.CommonTools;
import com.grumpycat.tetrisgame.tools.EventLog;
import com.grumpycat.tetrisgame.tools.NextView;
import com.grumpycat.tetrisgame.tools.OpDetector;
import com.grumpycat.tetrisgame.tools.SoundManager;


public class GameActivity extends AppCompatActivity implements UICallback,View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(AppCache.isBtnsLayoutDefault()?
                R.layout.activity_game:
                R.layout.activity_game2);
        init();
        measureViews();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private TextView tv_score;
    private TextView tv_line;
    private TextView tv_lvl;
    private OpDetector leftDetector;
    private OpDetector rightDetector;
    private NextView nextView;
    private Director director;
    private ImageView btn_left, btn_right;
    private void init() {
        director = Director.getInstance();
        director.setUiHandler(new UIHandler(this));
        SurfaceView sv_tetris = findViewById(R.id.sv_tetris);
        SurfaceHolder holder = sv_tetris.getHolder();
        holder.addCallback(new MyHolderCallback());
        sv_tetris.setKeepScreenOn(true);
        findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                director.rotate();
            }
        });

        findViewById(R.id.btn_fastdrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                director.fastDrop();
            }
        });

        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventLog.logClick("pause by btn");
                pauseGame();
            }
        });

        btn_left = findViewById(R.id.iv_btn_left);
        btn_right = findViewById(R.id.iv_btn_right);
        findViewById(R.id.btn_left).setOnTouchListener(this);
        findViewById(R.id.btn_right).setOnTouchListener(this);

        tv_line = findViewById(R.id.tv_line);
        tv_score = findViewById(R.id.tv_score);
        tv_lvl = findViewById(R.id.tv_lv);

        TextView tv_line_label = findViewById(R.id.tv_line_label);
        TextView tv_level_label = findViewById(R.id.tv_level_label);
        TextView tv_score_label = findViewById(R.id.tv_score_label);
        TextView tv_next_label = findViewById(R.id.tv_next_label);

        AppCache.setTypeface(tv_line);
        AppCache.setTypeface(tv_score);
        AppCache.setTypeface(tv_lvl);
        AppCache.setTypeface(tv_line_label);
        AppCache.setTypeface(tv_level_label);
        AppCache.setTypeface(tv_score_label);
        AppCache.setTypeface(tv_next_label);

        nextView = findViewById(R.id.next_view);

        leftDetector = new OpDetector(new OpDetector.OnOpListener() {
            @Override
            public void onDown() {
                btn_left.setImageResource(R.drawable.ic_btn_left_h);
            }

            @Override
            public void onUp() {
                btn_left.setImageResource(R.drawable.ic_btn_left);
            }

            @Override
            public void onOp() {
                director.moveLeft();
            }
        });

        rightDetector = new OpDetector(new OpDetector.OnOpListener() {
            @Override
            public void onDown() {
                btn_right.setImageResource(R.drawable.ic_btn_right_h);
            }

            @Override
            public void onUp() {
                btn_right.setImageResource(R.drawable.ic_btn_right);
            }

            @Override
            public void onOp() {
                director.moveRight();
            }
        });


        SoundManager.init(this);
        director.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventLog.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        director.stop();
        director.setUiHandler(null);
        director.setSurfaceHolder(null);
    }

    private void measureViews() {
        View ll_tetris = findViewById(R.id.ll_tetris);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenW = metrics.widthPixels;
        float density = metrics.density;
        int sceneW = (int) ((screenW - CommonTools.dp2px(20, density)) * 0.67f);
        ll_tetris.getLayoutParams().width = sceneW;
        ll_tetris.getLayoutParams().height = sceneW * 2;
    }

    @Override
    public void onNextMode(int mode) {
        nextView.setMode(mode);
    }


    @Override
    public void onLvlUp(int lvl) {
        this.lvl = lvl;
        tv_lvl.setText(lvl+"");
    }

    private int lvl;
    private int line;
    private int score;
    @Override
    public void onScoreUp(int score) {
        this.score = score;
        tv_score.setText(score+"");
    }

    @Override
    public void onLineUp(int line) {
        this.line = line;
        tv_line.setText(line+"");
    }

    private GameOverDialog gameOverDialog;
    @Override
    public void onGameOver() {
        if(isFinishing())
            return;
        EventLog.logGameOver(lvl, line, score);
        SharedPreferences sp = getSharedPreferences("records", MODE_PRIVATE);
        int bestHistory = sp.getInt("bestHistory", 0);
        if (score > bestHistory){
            sp.edit().putInt("bestHistory", score).apply();
        }
        if(gameOverDialog == null){
            gameOverDialog = new GameOverDialog(this, bestHistory, score) {
                @Override
                protected void restartGame() {
                    dismiss();
                    director.restart();
                    EventLog.logClick("restart");
                }

                @Override
                protected void exitGame() {
                    dismiss();
                    finish();
                    EventLog.logClick("exit when game over");
                }
            };
            gameOverDialog.setCancelable(false);
        }else{
            gameOverDialog.setScore(bestHistory, score);
        }

        gameOverDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.btn_left:
                leftDetector.onTouchEvent(event);
                break;
            case R.id.btn_right:
                rightDetector.onTouchEvent(event);
                break;
        }
        return true;
    }


    private class MyHolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            director.setSurfaceHolder(holder);
            director.autoResume();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            director.autoPause();
        }
    }

    @Override
    public void onBackPressed() {
        if(Director.getInstance().onBackPressed()){
            super.onBackPressed();
        }else{
            pauseGame();
            EventLog.logClick("pause by backPress");
        }
    }

    private PauseDialog pauseDialog;
    private void pauseGame(){
        director.pause();
        if(pauseDialog == null){
            pauseDialog = new PauseDialog(this) {
                @Override
                protected void continueGame() {
                    dismiss();
                    director.resume();
                }

                @Override
                protected void saveAndExit() {
                    dismiss();
                    Director.getInstance().save(getApplicationContext());
                    EventLog.logClick("save and exit");
                    finish();
                }

                @Override
                protected void exitGame() {
                    dismiss();
                    finish();
                    EventLog.logClick("exit when pause");
                }
            };
            pauseDialog.setCancelable(false);
        }
        pauseDialog.show();
    }
}
