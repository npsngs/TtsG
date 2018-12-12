package com.grumpycat.tetrisgame.core;

import org.json.JSONObject;

public interface IJsonData {
    JSONObject writeToJson() throws Exception;
    void readFromJson(JSONObject json) throws Exception;
}
