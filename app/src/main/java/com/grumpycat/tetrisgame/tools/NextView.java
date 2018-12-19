package com.grumpycat.tetrisgame.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.grumpycat.tetrisgame.core.TetrisContainer;
import com.grumpycat.tetrisgame.core.TetrisGenerator;
import com.grumpycat.tetrisgame.nodes.TetrisNode;

public class NextView extends View implements TetrisContainer{
    public NextView(Context context) {
        super(context);
        init();
    }

    public NextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        tetrisGenerator = new TetrisGenerator();
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(isModifyHeight)
                    return;
                isModifyHeight = true;
                int w = right -left;
                getLayoutParams().height = w;
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        size = size*3/4;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    public void setMode(int mode){
        this.mode = mode;
        invalidate();
    }

    private boolean isModifyHeight = false;
    private int mode = -1;
    private float unitMargin;
    private float unitSize;
    private TetrisGenerator tetrisGenerator;
    private RectF frameRect;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(frameRect == null){
            calculateSize();
        }
        TetrisNode tetrisNode = tetrisGenerator.getTetrisNode(mode, this);
        if(tetrisNode == null){
            return;
        }

        tetrisNode.setOffsetX(0);
        canvas.save();
        switch (mode){
            case 0:
                canvas.translate(unitSize/2,-unitSize/2);
                break;
            case 2:
                canvas.translate(unitSize/2,-unitSize/2);
                break;
            case 3:
                canvas.translate(unitSize/2,-unitSize/2);
                break;
            case 4:
                canvas.translate(unitSize/2,unitSize/2);
                break;
            case 5:
                canvas.translate(unitSize/2,unitSize/2);
                break;
            case 6:
                canvas.translate(unitSize, unitSize/2);
                break;
        }
        tetrisNode.draw(canvas, null);
        canvas.restore();
    }

    private void calculateSize(){
        unitMargin = 1;
        frameRect = new RectF(unitMargin+getPaddingLeft(),
                unitMargin+getPaddingTop(),
                getWidth()-unitMargin-getPaddingRight(),
                getHeight()-unitMargin-getPaddingBottom());
        unitSize = frameRect.width()/4;
    }

    @Override
    public int getRow() {
        return 3;
    }

    @Override
    public int getColumn() {
        return 4;
    }

    @Override
    public float getUnitSize() {
        return unitSize;
    }

    @Override
    public float getUnitMargin() {
        return unitMargin;
    }

    @Override
    public RectF getRealFrame() {
        return frameRect;
    }
}
