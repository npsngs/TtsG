package com.grumpycat.tetrisgame.core;

public class StatisticInfo {
    private int score;
    private int lines;
    private int lvl;
    private int lastTime;
    private long endTime;

    public void setScore(int score) {
        this.score = score;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getScore() {
        return score;
    }

    public int getLines() {
        return lines;
    }

    public int getLvl() {
        return lvl;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }
}
