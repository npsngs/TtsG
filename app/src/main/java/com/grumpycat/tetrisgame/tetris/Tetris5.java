package com.grumpycat.tetrisgame.tetris;

import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.nodes.TetrisNode;


/**
 *
 *    oo
 *     oo
 */
public class Tetris5 extends TetrisNode {
    public Tetris5(float unitSize, float unitMargin, RectF realFrame) {
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
        unitMatrix.set(0, 1, 5);
        unitMatrix.set(1, 1, 5);
        unitMatrix.set(1, 2, 5);
        unitMatrix.set(2, 2, 5);
    }

    @Override
    public void onDirectionChange(int direction) {
        unitMatrix.clear();
        switch (direction){
            case 0:
                unitMatrix.set(0, 0, 5);
                unitMatrix.set(1, 0, 5);
                unitMatrix.set(1, 1, 5);
                unitMatrix.set(2, 1, 5);
                break;
            case 1:
                unitMatrix.set(2, 0, 5);
                unitMatrix.set(2, 1, 5);
                unitMatrix.set(1, 1, 5);
                unitMatrix.set(1, 2, 5);
                break;
            case 2:
                unitMatrix.set(0, 1, 5);
                unitMatrix.set(1, 1, 5);
                unitMatrix.set(1, 2, 5);
                unitMatrix.set(2, 2, 5);
                break;
            case 3:
                unitMatrix.set(1, 0, 5);
                unitMatrix.set(1, 1, 5);
                unitMatrix.set(0, 1, 5);
                unitMatrix.set(0, 2, 5);
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
        if(x == 0){
            out[0] = 1;
            if(y == 2){
                out[1] = -1;
            }
        }else if(x == 2){
            if(y == 2){
                out[1] = -1;
            }else{
                return false;
            }
        }else if(x == 3){
            out[0] = -1;
            if(y == 2){
                out[1] = -1;
            }
        }
        return true;
    }
}
