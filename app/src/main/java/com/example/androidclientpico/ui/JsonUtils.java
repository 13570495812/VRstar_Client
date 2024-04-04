package com.example.androidclientpico.ui;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            AssetManager manager = context.getAssets();
            InputStream inputStream = manager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
        }
        return json;
    }

    public static void parseJSON(String json) {
//        Log.d(TAG, "======+++++++++++++++============Name: "+json);
        try {
//            JSONObject jsonObject = new JSONObject(json);
           JSONArray jsonArray = new JSONArray(json);
            Log.d(TAG, "==============Name: "+jsonArray);
//            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.getString("name");
                int age = item.getInt("age");
                Log.d(TAG, "Name: ");
                Log.d(TAG, "Name: " + name + ", Age: " + age);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }
}
