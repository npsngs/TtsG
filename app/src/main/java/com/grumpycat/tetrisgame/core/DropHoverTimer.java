package com.grumpycat.tetrisgame.core;

import com.grumpycat.tetrisgame.GameConfig;

public class DropHoverTimer {
    private long hoverTime;
    private long addTimes;
    public void startTimer(){
        hoverTime = GameConfig.DROP_HOVER_TIME;
        addTimes = GameConfig.DROP_HOVER_ADD_TIMES;
    }

    public void pastTime(long time){
        hoverTime -= time;
    }

    public void restartTimer(){
        if(addTimes > 0){
            addTimes--;
            hoverTime = GameConfig.DROP_HOVER_ADD_TIME;
        }
    }

    public boolean isTimeOver(){
        return hoverTime < 0L;
    }
}
