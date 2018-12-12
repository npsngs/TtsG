package com.grumpycat.tetrisgame.core;

import org.json.JSONObject;
import java.util.Random;

public class ModeGenerator {
    private final int weights[]={5,6,5,5,5,5,4};
    private int preMode;
    private int curMode;
    private int nextMode;
    private Random random;
    private int repeatCount;
    private int MAX;
    private int percents[];
    public ModeGenerator() {
        random = new Random();
        MAX = 0;
        percents = new int[weights.length];
        for(int i=0;i<weights.length;i++){
            MAX += weights[i];
            percents[i] = MAX;
        }
        preMode = curMode = next();
        nextMode = next();
        repeatCount = curMode == nextMode?1:0;
    }

    public ModeGenerator(int curMode, int nextMode, int repeatCount) {
        this.curMode = curMode;
        this.nextMode = nextMode;
        this.repeatCount = repeatCount;
        preMode = curMode;
    }

    public int getCurMode(){
        return curMode;
    }

    public int getNextMode() {
        return nextMode;
    }

    public int next() {
        preMode = curMode;
        int ret = nextMode;
        curMode = nextMode;
        nextMode = nextMode();

        if(repeatCount > 0){
            while(nextMode == curMode){
                nextMode = nextMode();
            }
            repeatCount = 0;
        }else{
            if(nextMode == curMode){
                repeatCount++;
            }else{
                repeatCount = 0;
            }
        }
        return ret;
    }

    private int nextMode(){
        int r = random.nextInt(MAX);
        int p = 0;
        for(int i=0;i< weights.length; i++){
            if(r >= p && r < weights[i]){
                return i;
            }
        }
        return random.nextInt(weights.length);
    }

    public JSONObject writeToJson() throws Exception{
        JSONObject json = new JSONObject();
        json.put("curMode", preMode);
        json.put("next", curMode);
        json.put("repeatCount", repeatCount);
        return json;
    }

    public void readFromJson(JSONObject json) throws Exception{
        curMode = json.getInt("curMode");
        nextMode = json.getInt("next");
        repeatCount = json.getInt("repeatCount");
    }
}
