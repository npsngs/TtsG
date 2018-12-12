package com.grumpycat.tetrisgame.core;

import com.grumpycat.tetrisgame.GameConfig;
public class GameCalculator {
    private static float MAX_SPEED = 18.0f;
    private static float BROKEN_SPEED = 14.0f;
    private static float[] LVL_SPEED = {
       0f, 4f, 6f, 8f,10f, 12f,BROKEN_SPEED
    };


    private int mode = 1;/* 1:  2:  3:*/
    public float speed;
    public float fastSpeed;
    private float unitSize;
    public void pastTime(long frameTime){
        if(mode == 2 && speedUpLeftTime > 0){
            speedUpLeftTime -= frameTime;
            if(speedUpLeftTime <= 0){
                speed = reverseSpeed * unitSize/1000;
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

    private long speedUpLeftTime;
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
                reverseSpeed += 0.2f;
                if (reverseSpeed >= MAX_SPEED) {
                    mode = 3;
                    speed = reverseSpeed * unitSize/1000;
                } else {
                    speed = MAX_SPEED* unitSize/ 1000;
                    speedUpLeftTime = GameConfig.SPEED_UP_TIME;
                }
            }
            break;
            case 3:
                reverseSpeed += 0.1f;
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

    public int calculateScore(int lineNum, int combo){
        return (lineNum+2)*(lineNum+3)*combo;
    }
}
