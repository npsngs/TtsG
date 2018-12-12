package com.grumpycat.tetrisgame.core;

import android.graphics.RectF;

public interface TetrisContainer {
    int getRow();
    int getColumn();
    float getUnitSize();
    float getUnitMargin();
    RectF getRealFrame();

}
