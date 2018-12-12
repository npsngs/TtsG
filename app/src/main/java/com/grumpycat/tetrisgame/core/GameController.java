package com.grumpycat.tetrisgame.core;

public interface GameController {
    void pause();
    void resume();
    void autoPause();
    void autoResume();
    void start();
    void stop();
    void restart();
}
