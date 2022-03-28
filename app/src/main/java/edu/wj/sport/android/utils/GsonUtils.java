package edu.wj.sport.android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public final class GsonUtils {


    public static final Gson GSON = new GsonBuilder().create();

    public static String toJson(Object object){
        if (object instanceof String){
            return (String) object;
        }else {
            return GSON.toJson(object);
        }
    }


    public static  <E> E toEntity(Class<E> e, Object object){
        String json = toJson(object);
        JsonElement jsonElement = JsonParser.parseString(json);
        if (jsonElement.isJsonArray()){
            return GSON.fromJson(jsonElement.getAsJsonArray().get(0), e);
        }
        return GSON.fromJson(json, e);
    }

    public static <E> List<E> toList(Class<E> e, Object object){
        String json = toJson(object);
        JsonElement jsonElement = JsonParser.parseString(json);
        List<E> list = new ArrayList<>();
        if (jsonElement.isJsonNull()){
            return list;
        }
        if (jsonElement.isJsonArray()){
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                list.add(GSON.fromJson(element, e));
            }
            return list;
        }
        list.add(GSON.fromJson(jsonElement, e));

        return list;
    }
}
