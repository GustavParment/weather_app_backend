package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.WeatherEntity;

import java.util.List;
import java.util.Optional;

public interface WeatherDao {
    List<WeatherEntity> getAllWeather();
    Optional<WeatherEntity> getWeatherByCity(String city);
    Optional<WeatherEntity> getWeatherById(Long id);
    boolean deleteWeatherById(Long id);
    WeatherEntity updateWeather(Long id, WeatherEntity updatedWeather);
    Optional<Double> getAverageTemperature(String cityName);
    WeatherEntity saveWeatherData(WeatherEntity weatherEntity);
}
