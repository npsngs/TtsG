package com.grumpycat.tetrisgame.nodes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.tools.AppCache;

import org.json.JSONObject;

import java.util.List;

public abstract class TetrisNode implements GameNode{
    private float unitSize;
    private float unitMargin;
    protected UnitMatrix unitMatrix;
    protected int direction = 0;
    private float speed;

    protected int offsetX = 0;
    protected int offsetY = 0;
    private int shadowY;
    protected float dropY = 0f;
    private Paint paint;
    private RectF unitRect;
    private RectF realFrame;
    private Bitmap[] unitMap;
    protected abstract UnitMatrix createUnitMatrix();
    protected abstract void onInit();
    public abstract void onDirectionChange(int direction);
    public abstract int getDirectionDimen();
    public abstract boolean reviseWhenCollide(int x, int y, int[] out);

    public TetrisNode(float unitSize, float unitMargin, RectF realFrame) {
        this.unitSize = unitSize;
        this.unitMargin = unitMargin;
        this.realFrame = realFrame;

        paint = new Paint();
        unitRect = new RectF();
        unitMap = AppCache.getUnitBitmaps();
        unitMatrix = createUnitMatrix();
        onInit();
    }

    public void initWithSize(float unitSize, float unitMargin, RectF realFrame) {
        this.unitSize = unitSize;
        this.unitMargin = unitMargin;
        this.realFrame = realFrame;
    }


    public final UnitMatrix getUnitMatrix() {
        return unitMatrix;
    }

    public void reset(){
        direction = 0;
        offsetY = 0;
        dropY = 0f;
        shadowY = -1;
        onInit();
    }

    private int left, right;
    @Override
    public void draw(Canvas canvas, Rect frame) {
       if(unitMatrix != null && unitMatrix.hasValidUnit()){
           List<int[]> validUnits = unitMatrix.getValidUnits();
           int r = 0;
           int l = unitMatrix.getColumn();
           for(int[] index: validUnits){
               int x = index[0];
               int y = index[1];
                if(x > r){
                    r = x;
                }

                if (x < l){
                    l = x;
                }

                if(shadowY > 0){
                    unitRect.set(unitMargin+(x+offsetX)*unitSize,
                            unitMargin+(y+shadowY)*unitSize,
                            (x+offsetX+1)*unitSize-unitMargin,
                            (y+shadowY+1)*unitSize-unitMargin);
                    unitRect.offset(realFrame.left,realFrame.top);
                    canvas.drawBitmap(AppCache.getShadow(), null, unitRect, paint);
                }

               unitRect.set(unitMargin+(x+offsetX)*unitSize,
                       unitMargin+(y+offsetY)*unitSize,
                       (x+offsetX+1)*unitSize-unitMargin,
                       (y+offsetY+1)*unitSize-unitMargin);
               unitRect.offset(realFrame.left,realFrame.top+dropY);
               int mode = unitMatrix.get(x, y);
               canvas.drawBitmap(unitMap[mode], null ,unitRect , paint);
           }

           left = l+offsetX;
           right = r+offsetX+1;
       }
    }

    public void setShadowY(int shadowY) {
        this.shadowY = shadowY;
    }

    public int getShadowY() {
        return shadowY;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public void rotate(){
        int dimen = getDirectionDimen();
        if(dimen == 1)
            return;
        direction++;
        direction = direction%getDirectionDimen();
        onDirectionChange(direction);
    }


    public void backwardRotate(){
        int dimen = getDirectionDimen();
        if(dimen == 1)
            return;

        direction += getDirectionDimen();
        direction -= 1;
        direction = direction%getDirectionDimen();
        onDirectionChange(direction);
    }

    public final void offsetLOrR(int step) {
        offsetX += step;
    }

    public final void offset(int x, int y) {
        offsetX += x;
        offsetY += y;
    }
    public void drop(float distance) {
        dropY += distance;
        while (dropY >= unitSize){
            dropY -= unitSize;
            offsetY++;
        }
    }

    public void enterShadowShot(){
        offsetY = shadowY;
        dropY = 0;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setDropY(float dropY) {
        this.dropY = dropY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public float getDropY() {
        return dropY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public JSONObject writeToJson() throws Exception{
        JSONObject json = new JSONObject();
        json.put("offsetX", offsetX);
        json.put("offsetY", offsetY);
        json.put("dropY", dropY);
        json.put("direction",direction);
        return json;
    }

    public void readFromJson(JSONObject json) throws Exception{
        offsetX = json.getInt("offsetX");
        offsetY = json.getInt("offsetY");
        dropY = json.getInt("dropY");
        direction = json.getInt("direction");
        onDirectionChange(direction);
    }


}
