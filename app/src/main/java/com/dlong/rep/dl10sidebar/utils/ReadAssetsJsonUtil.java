package com.dlong.rep.dl10sidebar.utils;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 读取assets资源文件夹下的JSON文件
 * @author  dlong
 * created at 2019/3/26 11:55 AM
 */
public class ReadAssetsJsonUtil {

    /**
     * 将JSON文件读取成JSONObject
     * @param fileName
     * @param context
     * @return
     */
    public static JSONArray getJSONArray(String fileName, Context context){
        try {
            return new JSONArray(getJson(fileName, context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJson(String fileName, Context context){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
