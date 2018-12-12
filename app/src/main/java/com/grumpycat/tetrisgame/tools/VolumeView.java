package com.grumpycat.tetrisgame.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.grumpycat.tetrisgame.R;

public class VolumeView extends View {
    public VolumeView(Context context) {
        super(context);
        init();
    }

    public VolumeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Drawable d1,d2,d3,d4;
    private boolean isInitSize = false;
    private float progress = 0.5f;
    private void init(){
        d1 = getResources().getDrawable(R.drawable.ic_progress_f);
        d2 = getResources().getDrawable(R.drawable.ic_progress_b);
        d3 = getResources().getDrawable(R.drawable.ic_controller);
        d4 = getResources().getDrawable(R.drawable.ic_volume_bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), d3.getIntrinsicHeight());
    }

    private int padding, n, step;
    private void initSize(){
        int h = getHeight();
        int w1 = d1.getIntrinsicWidth();
        int h1 = d1.getIntrinsicHeight();
        int h4 = d4.getIntrinsicHeight();
        d4.setBounds(0,(h-h4)/2, getWidth(), (h-h4)/2+h4);
        n = getWidth()/w1-1;
        padding = (getWidth() - n*w1)/2;
        d1.setBounds(padding, (h-h1)/2, padding+w1, (h-h1)/2+h1);
        d2.setBounds(padding, (h-h1)/2, padding+w1, (h-h1)/2+h1);
        d3.setBounds(padding-w1, 0, padding+d3.getIntrinsicWidth()-w1, h);
        step = w1;
        isInitSize = true;
    }

    public void setProgress(float p){
        this.progress = p;
        if(progress < 0){
            this.progress = 0;
        }else if(progress > 1f){
            this.progress = 1f;
        }
        invalidate();
    }


    private OnProgressChangeListener onProgressChangeListener;

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public interface OnProgressChangeListener{
        void onProgressChange(float progress);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                detectHit(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isHit){
                    moveCursor(event);
                }else{
                    detectHit(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isHit = false;
                break;
        }
        return true;
    }

    private boolean isHit = false;
    private float lastX;
    private void detectHit(MotionEvent event){
        float x = event.getX();
        int l = d3.getBounds().left;
        int r = d3.getBounds().right;
        int offset = (int) (n*progress)*step;
        int extras = d3.getIntrinsicWidth()*4;
        if(x> (l+offset-extras) && x < (r+offset +extras)){
            isHit = true;
            lastX = x;
        }
    }

    private void moveCursor(MotionEvent event){
        float x = event.getX();
        float delta = x - lastX;
        float r = delta/step/n;
        progress+=r;
        if(progress < 0){
            progress = 0;
        }else if(progress > 1f){
            progress = 1f;
        }

        if (onProgressChangeListener != null)
            onProgressChangeListener.onProgressChange(progress);

        lastX = x;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitSize)
            initSize();


        int p = (int) (n*progress);
        d4.draw(canvas);
        canvas.save();
        for(int i=0;i<n;i++){
            if(i < p){
                d1.draw(canvas);
            }else if(i > p){
                d2.draw(canvas);
            }else{
                d1.draw(canvas);
                d3.draw(canvas);
            }

            if(i < n-1){
                canvas.translate(step, 0);
            }
        }

        if(p == n){
            d3.draw(canvas);
        }
        canvas.restore();

    }
}
