package com.grumpycat.tetrisgame.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.grumpycat.tetrisgame.core.UnitMatrix;
import com.grumpycat.tetrisgame.tools.AppCache;

import java.util.List;

public class ShadowNode implements GameNode {
    private int shadowY;
    private TetrisNode tetrisNode;
    private RectF unitRect;
    private SceneNode sceneNode;
    private Paint paint;

    public ShadowNode(SceneNode sceneNode) {
        this.sceneNode = sceneNode;
        this.unitRect = new RectF();
        paint = new Paint();
    }

    public void setShadowY(int shadowY) {
        this.shadowY = shadowY;
    }

    public void setTetrisNode(TetrisNode tetrisNode) {
        this.tetrisNode = tetrisNode;
    }

    @Override
    public void draw(Canvas canvas, Rect frame) {
        UnitMatrix tetrisMatrix = tetrisNode.getUnitMatrix();
        int offsetX = tetrisNode.getOffsetX();
        List<int[]> indexs = tetrisMatrix.getValidUnits();
        for(int[] index:indexs) {
            int x = offsetX + index[0];
            int y = shadowY + index[1];

            RectF realFrame = sceneNode.getRealFrame();
            float unitSize = sceneNode.getUnitSize();
            float unitMargin = sceneNode.getUnitMargin();

            unitRect.set(realFrame.left + unitMargin + x * unitSize,
                    realFrame.top + unitMargin + y * unitSize,
                    realFrame.left + (x + 1) * unitSize - unitMargin,
                    realFrame.top + (y + 1) * unitSize - unitMargin);
            canvas.drawBitmap(AppCache.getShadow(), null, unitRect, paint);
        }
    }
}
