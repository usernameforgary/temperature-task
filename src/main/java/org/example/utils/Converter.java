package org.example.utils;

import okhttp3.Response;
import org.example.entity.Common;
import org.example.entity.WeatherInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Converter {
    private static final Logger logger = LoggerFactory.getLogger(Converter.class);

    // convert http response to Common entity
    public static <T extends Common> List<T> convertCommon(Response response, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        String respString = response.body().string();
        JSONObject json = null;
        try {
            json = new JSONObject(respString);
            Map<String, String> map = (HashMap) json.toMap();
            map.forEach((k, v) -> {
                try {
                    T t = clazz.newInstance();
                    t.setName(v);
                    t.setCode(k);
                    list.add(t);
                } catch (InstantiationException e) {
                    logger.error("cannot instance object, ", e.getMessage());
                } catch (IllegalAccessException e) {
                    logger.error("cannot instance object, ", e.getMessage());
                }
            });
        } catch (Exception e) {
            throw new Exception("cannot convert search result" + e.getMessage());
        }
        return list;
    }

    // convert http response to WeatherInfo entity
    public static WeatherInfo convertWeatherInfo(Response response) throws Exception {
        WeatherInfo weatherInfo = null;
        try {
            JSONObject json = new JSONObject(response.body().string());
            JSONObject weatherInfoJson = json.getJSONObject("weatherinfo");
            if(weatherInfoJson != null) {
                weatherInfo = new WeatherInfo();
                weatherInfo.setCity(weatherInfoJson.getString("city"));
                weatherInfo.setCityId(weatherInfoJson.getString("cityid"));
                weatherInfo.setTemp(weatherInfoJson.getString("temp"));
            }
        } catch (Exception e) {
            throw new Exception("cannot convert weather info result, " + e.getMessage());
        }

        return weatherInfo;
    }
}
