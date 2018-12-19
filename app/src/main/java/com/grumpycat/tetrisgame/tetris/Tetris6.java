package com.grumpycat.tetrisgame.tetris;

import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.nodes.TetrisNode;

/**
 *    oo
 *    oo
 *
 */
public class Tetris6 extends TetrisNode {
    public Tetris6(float unitSize, float unitMargin, RectF realFrame) {
        super(unitSize, unitMargin, realFrame);
        unitMatrix.set(0, 0, 6);
        unitMatrix.set(1, 1, 6);
        unitMatrix.set(0, 1, 6);
        unitMatrix.set(1, 0, 6);
    }

    @Override
    protected UnitMatrix createUnitMatrix() {
        return new UnitMatrix(2, 2);
    }

    @Override
    protected void onInit() {
        offsetX = 4;
    }

    @Override
    public void calculateShadowY(UnitMatrix matrix) {
        int ofsX = offsetX;
        int ofsY = offsetY;
        if(dropY > 0){
            ofsY++;
        }

        do {
            if(matrix.get(ofsX, ofsY+1) != UnitMatrix.NULL ||
                    matrix.get(ofsX+1, ofsY+1) != UnitMatrix.NULL){
                shadowY = ofsY-1;
                return;
            }
            ofsY++;
        }while (true);
    }

    @Override
    public void onDirectionChange(int direction) {}

    @Override
    public int getDirectionDimen() {
        return 1;
    }
}
