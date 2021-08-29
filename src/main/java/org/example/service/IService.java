package org.example.service;

import org.example.entity.Common;
import org.example.entity.WeatherInfo;

import java.util.List;

public interface IService {
     <T extends Common> List<T> getCommon(String url, Class<T> clazz);

     WeatherInfo getWeatherInfoByCountryUrl(String url);
}
