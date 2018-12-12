package com.grumpycat.tetrisgame.tetris;

import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.nodes.TetrisNode;

/**
 *
 *    ooo
 *    o
 */
public class Tetris2 extends TetrisNode {
    public Tetris2(float unitSize, float unitMargin, RectF realFrame) {
        super(unitSize, unitMargin, realFrame);
    }

    @Override
    protected UnitMatrix createUnitMatrix() {
        return new UnitMatrix(3, 3);
    }

    @Override
    protected void onInit() {
        offsetX = 3;
        unitMatrix.clear();
        unitMatrix.set(0, 1, 2);
        unitMatrix.set(1, 1, 2);
        unitMatrix.set(2, 1, 2);
        unitMatrix.set(0, 2, 2);
    }

    @Override
    public void onDirectionChange(int direction) {
        unitMatrix.clear();
        switch (direction){
            case 0:
                unitMatrix.set(0, 1, 2);
                unitMatrix.set(1, 1, 2);
                unitMatrix.set(2, 1, 2);
                unitMatrix.set(0, 2, 2);
                break;
            case 1:
                unitMatrix.set(0, 0, 2);
                unitMatrix.set(1, 0, 2);
                unitMatrix.set(1, 1, 2);
                unitMatrix.set(1, 2, 2);
                break;
            case 2:
                unitMatrix.set(0, 1, 2);
                unitMatrix.set(1, 1, 2);
                unitMatrix.set(2, 1, 2);
                unitMatrix.set(2, 0, 2);
                break;
            case 3:
                unitMatrix.set(2, 2, 2);
                unitMatrix.set(1, 0, 2);
                unitMatrix.set(1, 1, 2);
                unitMatrix.set(1, 2, 2);
                break;


        }
    }

    @Override
    public int getDirectionDimen() {
        return 4;
    }

    @Override
    public boolean reviseWhenCollide(int x, int y, int[] out) {
        if(x == 1 && y == 1){
            return false;
        }
        out[0] = 0;
        out[1] = 0;
        switch (direction){
            case 0:
            case 2:
                if(x == 0){
                    out[0] = 1;
                }else{
                    out[0] = -1;
                }
                break;
            case 1:
                if(x == 0){
                    out[0] = 1;
                }else if(y == 2){
                    out[1] = -1;
                }else {
                    return false;
                }
                break;
            case 3:
                if(x == 2){
                    out[0] = -1;
                }else if(y == 2){
                    out[1] = -1;
                }else {
                    return false;
                }
                break;
        }
        return true;
    }
}
