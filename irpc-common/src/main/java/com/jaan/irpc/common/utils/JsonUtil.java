package com.jaan.irpc.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @ClassName JsonUtil
 * @Description
 * @Author jaan
 * @Date 2022/10/15 18:10
 */
public class JsonUtil {

    private static  Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }

    public static <T> T fromJson(String json, Class<T> classOfT){
        return GSON.fromJson(json,classOfT);
    }

    public static <T> T  fromJson(String json, Type typeOfT){
        return GSON.fromJson(json,typeOfT);
    }

    public static String toJson(Object obj){
        return GSON.toJson(obj);
    }

}
