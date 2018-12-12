package com.grumpycat.tetrisgame.tools;

import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;

import com.grumpycat.tetrisgame.GameConfig;

public class SceneShaker {
    private boolean isShaking = false;
    private long shakeTime = 0L;
    private Interpolator interpolator;
    public void startShake(){
        isShaking = true;
        shakeTime = 0L;
        interpolator = new CycleInterpolator(2f);
    }

    public float shakeScene(long frameTime){
        shakeTime += frameTime;
        if(shakeTime >= GameConfig.SCENE_SHAKE_TIME){
            shakeTime = GameConfig.SCENE_SHAKE_TIME;
            isShaking = false;
        }
        return interpolator.getInterpolation(shakeTime*1f/GameConfig.SCENE_SHAKE_TIME);
    }

    public boolean isShaking() {
        return isShaking;
    }
}
