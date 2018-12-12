package com.grumpycat.tetrisgame.core;

public interface UICallback {

    void onNextMode(int mode);

    void onLvlUp(int lvl);

    void onScoreUp(int score);

    void onLineUp(int line);

    void onGameOver();
}
