package com.grumpycat.tetrisgame.core;


public class TetrisOp implements IGameOp {
    private boolean isNeedFastDrop;
    private boolean isNeedRotate;
    private int moveStep;
    public void reset(){
        isNeedFastDrop = false;
        isNeedRotate = false;
        moveStep = 0;
    }

    public boolean isNop(){
        return !isNeedFastDrop && !isNeedRotate && moveStep ==0;
    }

    public boolean isNeedFastDrop() {
        return isNeedFastDrop;
    }

    public boolean isNeedRotate() {
        return isNeedRotate;
    }

    public int getMoveStep() {
        return moveStep;
    }

    public void resetFastDrop() {
        isNeedFastDrop = false;
    }

    public void resetNeedRotate() {
        isNeedRotate = false;
    }

    public void resetMoveStep() {
        this.moveStep = 0;
    }

    @Override
    public void rotate() {
        isNeedRotate = true;
    }

    @Override
    public void moveLeft() {
        moveStep = -1;
    }

    @Override
    public void moveRight() {
        moveStep = 1;
    }


    @Override
    public void fastDrop() {
        isNeedFastDrop = true;
        isNeedRotate = false;
        moveStep = 0;
    }

}
