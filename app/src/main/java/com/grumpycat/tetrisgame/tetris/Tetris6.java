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
    }

    @Override
    protected UnitMatrix createUnitMatrix() {
        return new UnitMatrix(2, 2);
    }

    @Override
    protected void onInit() {
        offsetX = 4;
        unitMatrix.clear();
        unitMatrix.set(0, 0, 6);
        unitMatrix.set(1, 1, 6);
        unitMatrix.set(0, 1, 6);
        unitMatrix.set(1, 0, 6);
    }

    @Override
    public void onDirectionChange(int direction) {
    }

    @Override
    public int getDirectionDimen() {
        return 1;
    }

    @Override
    public boolean reviseWhenCollide(int x, int y, int[] out) {
        return false;
    }
}
