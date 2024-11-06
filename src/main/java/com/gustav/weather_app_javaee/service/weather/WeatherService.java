package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface WeatherService {

    List<WeatherEntity> getAllWeather();
    WeatherEntity getWeatherByCity(String city);
    Optional<WeatherEntity> getWeatherById(Long id);
    boolean deleteWeatherById(Long id);
    WeatherEntity updateWeather(Long id, WeatherDTO weatherDTO);
    Mono<WeatherEntity> fetchAndSaveWeatherData(String cityName);
    WeatherEntity addWeather(String cityName, WeatherDTO weather);
    Optional<Double> getAverageTemperature(String cityName);

}//TODO getWeatherByCity kanske lite onödig?
