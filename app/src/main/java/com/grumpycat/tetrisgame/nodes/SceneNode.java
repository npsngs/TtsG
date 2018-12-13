package com.grumpycat.tetrisgame.nodes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.grumpycat.tetrisgame.GameConfig;
import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.core.TetrisContainer;
import com.grumpycat.tetrisgame.tools.AppCache;

import org.json.JSONObject;

import java.util.List;

public class SceneNode implements GameNode , TetrisContainer{
    private static final int[][] RANDOM_ADD_LINES= {
            {0,0,1,1,1,1,1,0,0,0},
            {1,0,0,1,1,1,0,1,1,0},
            {0,1,1,0,0,1,0,0,1,1},
            {1,0,1,1,0,0,1,1,1,1},
            {1,1,0,1,1,0,0,1,1,1},
            {0,1,1,1,1,1,0,0,1,0},
            {1,1,1,1,1,1,1,1,0,0},
            {1,1,1,1,1,1,0,0,0,1},
            {1,1,0,0,0,0,1,1,1,1},
            {0,1,0,0,1,0,0,1,0,1},
            {1,0,1,0,1,0,1,0,0,0},
            {1,0,0,0,1,0,0,0,1,1},
            {1,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,1,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,0,1,1,1,1,1,1},
            {1,1,0,1,1,1,1,1,1,1},
            {1,1,0,1,1,1,1,1,1,1},
            {0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,1,1,0,1,0,0},
            {0,0,0,0,1,0,1,0,0,0},
            {0,1,0,0,1,0,0,0,0,0},
    };
    private float unitSize;
    private float unitMargin;

    private int column = 10;
    private int row;
    private Rect frameRect;
    private UnitMatrix unitMatrix;
    private Paint gridLinePaint;
    private float[] gridPts;
    private RectF unitRect;
    private RectF realFrame;
    private Bitmap[] unitMap;
    private Paint unitPaint;
    private boolean isOnDelete = false;
    private long deleteTime = 0;
    public SceneNode() {
        unitMargin = AppCache.getUnitMargin();
        unitRect = new RectF();
        unitMap = AppCache.getUnitBitmaps();
        unitPaint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.75f);
        unitPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        realFrame = new RectF();
        frameRect = new Rect(-1, -1, -1, -1);
        gridLinePaint = new Paint();
        gridLinePaint.setColor(0xff252525);
        gridLinePaint.setStrokeWidth(unitMargin*2);
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        if(GameConfig.isShowGrid){
            int h = frame.height();
            if(gridPts == null){
                gridPts = new float[(row+column+2)*4];
                int ptr = 0;
                int w = frame.width();
                for(int i=0;i<=row;i++){
                    gridPts[ptr] = unitMargin;
                    ptr++;
                    gridPts[ptr] = h-unitMargin-unitSize*i;
                    ptr++;
                    gridPts[ptr] = w-unitMargin;
                    ptr++;
                    gridPts[ptr] = h-unitMargin-unitSize*i;
                    ptr++;
                }

                for(int i=0;i<=column;i++){
                    gridPts[ptr] = unitSize*i+unitMargin;
                    ptr++;
                    gridPts[ptr] = h;
                    ptr++;
                    gridPts[ptr] = unitSize*i+unitMargin;
                    ptr++;
                    gridPts[ptr] = 0;
                    ptr++;
                }
            }


            canvas.drawLines(gridPts, gridLinePaint);
        }





        if(!isOnDelete) {
            List<int[]> validUnits = unitMatrix.getValidUnits();
            for (int[] index : validUnits) {
                int x = index[0];
                int y = index[1];

                unitRect.set(realFrame.left + unitMargin + x * unitSize,
                        realFrame.top + unitMargin + y * unitSize,
                        realFrame.left + (x + 1) * unitSize - unitMargin,
                        realFrame.top + (y + 1) * unitSize - unitMargin);
                int mode = unitMatrix.get(x, y);
                canvas.drawBitmap(unitMap[mode], null, unitRect, unitPaint);
            }
        }else{
           int[][] units = unitMatrix.getUnits();
           for(int x =0; x< column;x++){
               for(int y=0;y<row;y++){
                   int mode = units[x][y];
                   if (units[x][y] == UnitMatrix.NULL)
                       continue;

                   unitRect.set(realFrame.left + unitMargin + x * unitSize,
                           realFrame.top + unitMargin + y * unitSize,
                           realFrame.left + (x + 1) * unitSize - unitMargin,
                           realFrame.top + (y + 1) * unitSize - unitMargin);
                   canvas.drawBitmap(unitMap[mode], null, unitRect, unitPaint);
               }
           }
        }
    }

    public void addRandomLine(int factor){
        int index = factor%RANDOM_ADD_LINES.length;
        unitMatrix.addBottomLine(RANDOM_ADD_LINES[index]);
    }

    public void initWithFrame(Rect frame){
        if(!frameRect.equals(frame)){
            frameRect.set(frame);
            unitSize = (frame.width()*1f-2*unitMargin)/10;
            row = (int) ((frame.height()*1f-2*unitMargin)/unitSize);
            row += 2;
            realFrame.set(unitMargin,
                    frame.height() -unitMargin-row*unitSize,
                    frame.width()-unitMargin,
                    frame.height() -unitMargin);

            if(unitMatrix == null
                    || unitMatrix.getRow() != row ){
                unitMatrix = new UnitMatrix(row, column);
            }
        }
    }

    public void reset(){
        isOnDelete = false;
        if (unitMatrix != null)
            unitMatrix.clear();
    }

    @Override
    public RectF getRealFrame() {
        return realFrame;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    public float getUnitSize() {
        return unitSize;
    }

    @Override
    public float getUnitMargin() {
        return unitMargin;
    }

    public UnitMatrix getUnitMatrix() {
        return unitMatrix;
    }


    public boolean isOnDelete() {
        return isOnDelete;
    }

    public void startDelete(){
        isOnDelete = true;
        deleteTime = 0;
    }

    public void deleteLines(long frameTime, int[] lines){
        deleteTime += frameTime;
        float value = deleteTime*1f/GameConfig.DELETE_ANIM_TIME;
        if(value < 1f){
            int middle = column/2;
            int step = (int) (middle*value);
            for (int line:lines){
                if(line < 0) continue;
                unitMatrix.set(middle-step-1, line, UnitMatrix.NULL);
                unitMatrix.set(middle+step, line, UnitMatrix.NULL);
            }
        }else{
            unitMatrix.deleteLines(lines);
            isOnDelete = false;
        }
    }


    public float getLineY(int line){
        if(line < 0)
            return -1f;
        if(line >= row-1)
            return frameRect.height()-unitSize;
        return frameRect.height() - (row-line)*unitSize;
    }


    public JSONObject writeToJson() throws Exception{
        JSONObject json = new JSONObject();
        json.put("isOnDelete",isOnDelete);
        json.put("deleteTime",deleteTime);
        json.put("unitSize",unitSize);
        json.put("unitMatrix", UnitMatrix.writeToJson(unitMatrix));
        return json;
    }

    public void readFromJson(JSONObject json) throws Exception{
        isOnDelete = json.getBoolean("isOnDelete");
        deleteTime = json.getLong("deleteTime");
        unitSize = json.getInt("unitSize");
        unitMatrix = UnitMatrix.readFromJson(json.getJSONObject("unitMatrix"));
        row = unitMatrix.getRow();
    }
}
