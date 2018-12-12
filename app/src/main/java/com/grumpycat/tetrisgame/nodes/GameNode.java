package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface GameNode {
    void draw(Canvas canvas, Rect frame);
}
