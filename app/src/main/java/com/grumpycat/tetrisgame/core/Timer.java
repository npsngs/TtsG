package com.grumpycat.tetrisgame.core;

import org.json.JSONObject;

public class Timer implements IJsonData{
    private long maxTime;
    private long curTime;

    public Timer(long maxTime) {
        this.maxTime = maxTime;
    }

    public void start(){
        curTime = maxTime;
    }

    public void pastTime(long time){
        if (time > 0L)
            curTime -= time;
    }

    public boolean isTimeOver(){
        return curTime < 0L;
    }

    @Override
    public JSONObject writeToJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("curTime",curTime);
        return json;
    }

    @Override
    public void readFromJson(JSONObject json) throws Exception {
        curTime = json.getLong("curTime");
    }
}
