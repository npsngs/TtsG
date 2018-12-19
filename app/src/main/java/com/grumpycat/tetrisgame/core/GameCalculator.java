package com.grumpycat.tetrisgame.core;

import com.grumpycat.tetrisgame.GameConfig;
public abstract class GameCalculator {
    private static final float MAX_SPEED = 18.0f;
    private static final float BROKEN_SPEED = 14.0f;
    private static final float[] LVL_SPEED = {
       0f, 4f, 6f, 8f,10f, 13f,BROKEN_SPEED
    };


    private int mode = 1;/* 1:  2:  3:*/
    public float speed;
    public float fastSpeed;
    private float unitSize;
    public void pastTime(long frameTime){
        if(mode == 2 && leftTime > 0){
            leftTime -= frameTime;
            if(leftTime <= 0){
                speed = reverseSpeed * unitSize/1000;
            }
        }else if(mode == 3){
            leftTime -= frameTime;
            if(leftTime < 0){
                leftTime = GameConfig.ADD_LINE_INTERVAL;
                onAddLine();
            }
        }
    }

    public void reset(){
        mode = 1;
    }

    public void setUnitSize(float unitSize) {
        this.unitSize = unitSize;
        fastSpeed = unitSize / 4;
    }

    private long leftTime;
    private float reverseSpeed;
    public void onLevelUp(int lv){
        switch (mode){
            case 1: {
                if (lv < LVL_SPEED.length) {
                    if (lv == LVL_SPEED.length - 1) {
                        mode = 2;
                        reverseSpeed = BROKEN_SPEED;
                    }
                    speed = LVL_SPEED[lv] * unitSize / 1000;
                }
            }
            break;
            case 2: {
                reverseSpeed += 1.0f;
                if (reverseSpeed >= MAX_SPEED) {
                    mode = 3;
                    leftTime = GameConfig.ADD_LINE_INTERVAL;
                    speed = reverseSpeed * unitSize/1000;
                } else {
                    speed = MAX_SPEED* unitSize/ 1000;
                    leftTime = GameConfig.SPEED_UP_TIME;
                }
            }
            break;
            case 3:
                reverseSpeed += 0.2f;
                speed = reverseSpeed * unitSize/1000;
                break;
        }
    }

    public int calculateLvl(int lines){
        if(lines <0)
            return 1;

        int lv;
        if(lines < 20){
            lv = lines/5+1;
        }else if(lines < 60){
            lv = (lines - 20)/10 + 5;
        }else if(lines < 160){
            lv = (lines - 60)/20 + 9;
        }else{
            lv = (lines - 160)/40 + 13;
        }
        return lv;
    }

    public int calculateScore(int lineNum, int lv){
        switch (lineNum){
            case 1:
               return lv;
            case 2:
                return lv*8;
            case 3:
                return lv*27;
            case 4:
                return lv*64;
            default:
                return 0;
        }
    }

    protected abstract void onAddLine();
}
