package com.grumpycat.tetrisgame.tools;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.grumpycat.tetrisgame.R;

public class EnterAnimView extends View {
    private final long anim_duration = 600L;

    public EnterAnimView(Context context) {
        super(context);
        init();
    }

    public EnterAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnterAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EnterAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private float density;
    private Drawable drawable1;
    private Drawable drawable2;
    private Drawable drawable3;
    private int startY1, startY2, targetY1, targetY2;
    private float fraction = 0.0f;
    private boolean isLayout = false;

    private Interpolator interpolator, dece, acce;
    private void init(){
        Resources res = getResources();
        density = res.getDisplayMetrics().density;
        drawable1 = res.getDrawable(R.drawable.ic_bg1);
        drawable2 = res.getDrawable(R.drawable.ic_bg2);
        drawable3 = res.getDrawable(R.drawable.ic_bg3);

        dece = new DecelerateInterpolator();
        acce = new AccelerateInterpolator();
        interpolator = dece;
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                setBounds(drawable1, left, top, right, bottom);
                setBounds(drawable2, left, top, right, bottom);
                drawable3.setBounds(0,0, drawable3.getIntrinsicWidth(), drawable3.getIntrinsicHeight());

                Rect bounds1 = drawable1.getBounds();
                Rect bounds2 = drawable2.getBounds();
                Rect bounds3 = drawable3.getBounds();

                int reviseY = CommonTools.dp2px(14f, density);

                int offsetY2 = getHeight()/2-bounds2.centerY()+reviseY;
                int offsetY1 = offsetY2 - 2*bounds1.height() /7 - CommonTools.dp2px(0.5f, density);
                bounds1.offset(0, offsetY1);
                bounds2.offset(0, offsetY2);

                startY1 = bounds1.top;
                startY2 = bounds2.top;
                targetY1 = 0;
                targetY2 = getHeight() - bounds2.height();

                bounds3.offset(getWidth()/2 -bounds3.centerX(), getHeight()/2- bounds3.centerY());
                isLayout = true;

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open();
                    }
                },600);
            }
        });
    }


    public void open(){
        if (!isLayout || fraction!= 0.0f)
             return;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(anim_duration*2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                interpolator = dece;
                if (onAnimLister != null)
                    onAnimLister.onStartOpen();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fraction = 1.0f;
                invalidate();
            }
        });
        animator.start();
    }


    public void close(){
        if (!isLayout || fraction != 1.0f)
            return;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(anim_duration/2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = 1.0f - animation.getAnimatedFraction();
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                interpolator = acce;
                if (onAnimLister != null)
                    onAnimLister.onStartClose();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimLister != null)
                    onAnimLister.onCloseFinish();
            }
        });
        animator.start();
    }
    private OnAnimLister onAnimLister;
    public void setOnAnimLister(OnAnimLister onAnimLister) {
        this.onAnimLister = onAnimLister;
    }

    public interface OnAnimLister {
        void onCloseFinish();
        void onStartOpen();
        void onStartClose();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0, fraction*(targetY1 - startY1));
        drawable1.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(0, fraction*(targetY2 - startY2));
        drawable2.draw(canvas);
        canvas.restore();

        drawable3.setAlpha((int) (255*(1f-interpolator.getInterpolation(fraction))));
        drawable3.draw(canvas);
    }

    private void setBounds(Drawable d, int left, int top, int right, int bottom){
        int ih = d.getIntrinsicHeight();
        int iw = d.getIntrinsicWidth();
        int w = right-left;
        int h = ih*w/iw;

        Rect bounds = new Rect(0,0,w,h);
        d.setBounds(bounds);
    }

}
