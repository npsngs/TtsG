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
    private static final int[][] reviseOffsets = {
            {0, -1},
            {1, 0},
            {-1, 0},
            {1, -1},
            {-1, -1},
            {0, -2},
            {2, 0},
            {-2, 0},
            {2, -2},
            {-2, -2},
            {0, -3},
            {0, 1}
    };
    private float unitSize;
    private float unitMargin;
    protected UnitMatrix unitMatrix;
    protected int direction = 0;
    private float speed;

    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int shadowY;
    protected float dropY = 0f;
    private Paint paint;
    private RectF unitRect;
    private RectF realFrame;
    private Bitmap[] unitMap;
    protected abstract UnitMatrix createUnitMatrix();
    protected abstract void onInit();
    public abstract void onDirectionChange(int direction);
    public abstract int getDirectionDimen();
    public abstract void calculateShadowY(UnitMatrix matrix);
    public TetrisNode(float unitSize, float unitMargin, RectF realFrame) {
        this.unitSize = unitSize;
        this.unitMargin = unitMargin;
        this.realFrame = realFrame;

        paint = new Paint();
        unitRect = new RectF();
        unitMap = AppCache.getUnitBitmaps();
        unitMatrix = createUnitMatrix();
        reset();
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
        onInit();
        direction = 0;
        offsetY = 0;
        dropY = 0f;
        shadowY = -1;
        onDirectionChange(direction);
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

                float left = unitMargin+(x+offsetX)*unitSize;
                float right = (x+offsetX+1)*unitSize-unitMargin;
                float top = unitMargin+(y+offsetY)*unitSize;
                float bottom = (y+offsetY+1)*unitSize-unitMargin;
                if(shadowY > 0){
                    float offset = (shadowY-offsetY)*unitSize;
                    unitRect.set(left, top, right, bottom);
                    unitRect.offset(realFrame.left,realFrame.top+offset);
                    canvas.drawBitmap(AppCache.getShadow(), null, unitRect, paint);
                }

               unitRect.set(left, top, right, bottom);
               unitRect.offset(realFrame.left,realFrame.top+dropY);
               int mode = unitMatrix.get(x, y);
               canvas.drawBitmap(unitMap[mode], null ,unitRect , paint);
           }

           left = l+offsetX;
           right = r+offsetX+1;
       }
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

    public boolean rotate(UnitMatrix matrix){
        int dimen = getDirectionDimen();
        if(dimen == 1)
            return true;
        direction++;
        direction = direction%getDirectionDimen();
        onDirectionChange(direction);
        if(!onRotate(matrix)){
            backwardRotate();
            return false;
        }
        return true;
    }

    protected boolean onRotate(UnitMatrix matrix) {
        if(!hasCollide(matrix, 0,0)){
            return true;
        }

        for(int[] offset:reviseOffsets){
            if(!hasCollide(matrix, offset[0], offset[1])){
                offsetX += offset[0];
                offsetY += offset[1];
                return true;
            }
        }
        return false;
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

    public boolean hasCollide(UnitMatrix matrix, int x, int y){
        int ox = this.offsetX+x;
        int oy = this.offsetY+y;
        if(dropY > 0){
            oy++;
        }
        List<int[]> indexs = unitMatrix.getValidUnits();
        for(int[] index:indexs) {
            int sx = ox + index[0];
            int sy = oy + index[1];
            int value = matrix.get(sx, sy);
            if (value != UnitMatrix.NULL) {
                return true;
            }
        }
        return false;
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
        json.put("shadowY",shadowY);
        return json;
    }

    public void readFromJson(JSONObject json) throws Exception{
        offsetX = json.getInt("offsetX");
        offsetY = json.getInt("offsetY");
        dropY = json.getInt("dropY");
        direction = json.getInt("direction");
        shadowY = json.getInt("shadowY");
        onDirectionChange(direction);
    }


}
