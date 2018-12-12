package com.grumpycat.tetrisgame.core;

import com.grumpycat.tetrisgame.GameConfig;
import org.json.JSONObject;

public class ComboCounter implements IJsonData{
    private int combo = 0;
    private long passTime = 0;
    private float radio = 1.0f;
    public int hit(int base){
        if (passTime > GameConfig.COMBO_INTERVAL){
            combo = base;
        }else{
            combo += base;
        }
        passTime = 0;
        radio = 1.0f;
        return combo;
    }

    public void pastTime(long time){
        if (combo <= 0)return;

        passTime += time;
        radio = (GameConfig.COMBO_INTERVAL-passTime)*1.0f/GameConfig.COMBO_INTERVAL;

        if (radio <= 0f){
            combo = 0;
            radio = 0f;
        }
    }

    public float getRadio() {
        return radio;
    }

    public int getCombo() {
        return combo;
    }
    public boolean isOnCombo(){
        return combo > 0;
    }

    public void reset(){
        combo = 0;
        passTime = 0;
        radio = 1.0f;
    }


    @Override
    public JSONObject writeToJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("combo", combo);
        json.put("passTime",passTime);
        return json;
    }

    @Override
    public void readFromJson(JSONObject json) throws Exception {
        combo = json.getInt("combo");
        passTime = json.getLong("passTime");
        radio = (GameConfig.COMBO_INTERVAL-passTime)*1.0f/GameConfig.COMBO_INTERVAL;
    }
}
