package com.grumpycat.tetrisgame.tetris;

import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.nodes.TetrisNode;

/**
 *
 *    oooo
 *
 */
public class Tetris1 extends TetrisNode {
    public Tetris1(float unitSize, float unitMargin, RectF realFrame) {
        super(unitSize, unitMargin, realFrame);
    }

    @Override
    protected UnitMatrix createUnitMatrix() {
        return new UnitMatrix(4, 4);
    }

    @Override
    protected void onInit() {
        offsetX = 3;
    }

    @Override
    public void onDirectionChange(int direction) {
        unitMatrix.clear();
        switch (direction){
            case 0:
                unitMatrix.set(0, 1, 1);
                unitMatrix.set(1, 1, 1);
                unitMatrix.set(2, 1, 1);
                unitMatrix.set(3, 1, 1);
                break;
            case 1:
                unitMatrix.set(2, 0, 1);
                unitMatrix.set(2, 1, 1);
                unitMatrix.set(2, 2, 1);
                unitMatrix.set(2, 3, 1);
                break;
            case 2:
                unitMatrix.set(0, 2, 1);
                unitMatrix.set(1, 2, 1);
                unitMatrix.set(2, 2, 1);
                unitMatrix.set(3, 2, 1);
                break;
            case 3:
                unitMatrix.set(1, 0, 1);
                unitMatrix.set(1, 1, 1);
                unitMatrix.set(1, 2, 1);
                unitMatrix.set(1, 3, 1);
                break;
        }
    }

    @Override
    public int getDirectionDimen() {
        return 4;
    }


    @Override
    public void calculateShadowY(UnitMatrix matrix) {
        int ofsX = offsetX;
        int ofsY = offsetY;
        if(dropY > 0){
            ofsY++;
        }

        do {
            switch (direction){
                case 0:
                    if(matrix.get(ofsX, ofsY+1) != UnitMatrix.NULL ||
                            matrix.get(ofsX+1, ofsY+1) != UnitMatrix.NULL ||
                            matrix.get(ofsX+2, ofsY+1) != UnitMatrix.NULL ||
                            matrix.get(ofsX+3, ofsY+1) != UnitMatrix.NULL){
                        shadowY = ofsY-1;
                        return;
                    }break;
                case 1:
                    if(matrix.get(ofsX+2, ofsY+3) != UnitMatrix.NULL){
                        shadowY = ofsY-1;
                        return;
                    }break;
                case 2:
                    if(matrix.get(ofsX, ofsY+2) != UnitMatrix.NULL ||
                            matrix.get(ofsX+1, ofsY+2) != UnitMatrix.NULL ||
                            matrix.get(ofsX+2, ofsY+2) != UnitMatrix.NULL ||
                            matrix.get(ofsX+3, ofsY+2) != UnitMatrix.NULL){
                        shadowY = ofsY-1;
                        return;
                    }break;
                case 3:
                    if(matrix.get(ofsX+1, ofsY+3) != UnitMatrix.NULL){
                        shadowY = ofsY-1;
                        return;
                    }break;
            }
            ofsY++;
        }while (true);
    }
}
