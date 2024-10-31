package com.gustav.weather_app_javaee.service;

import com.gustav.weather_app_javaee.dto.WeatherDTO;
import com.gustav.weather_app_javaee.dto.WeatherDataList;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface WeatherService {

    List<WeatherEntity> getAllWeather();
    WeatherEntity getWeatherByCity(String city);
    Optional <WeatherEntity> getWeatherById(Long id);
    boolean deleteWeatherById(Long id);
    WeatherEntity updateWeather(Long id,WeatherDTO weatherDTO);
    WeatherEntity convertToEntity(String cityName, WeatherDTO weather);
    Mono <WeatherEntity> fetchAndSaveWeatherData(String cityName);
    WeatherEntity addWeather(String cityName, WeatherDTO weather);
    Optional <Double> getAverageTemperature(String cityName);


}
