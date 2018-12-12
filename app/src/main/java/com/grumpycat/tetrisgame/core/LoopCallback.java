package com.grumpycat.tetrisgame.core;

import android.os.Looper;

public interface LoopCallback {
    void onLoopStart(Looper looper);
    void loop(long frameTime);
}
