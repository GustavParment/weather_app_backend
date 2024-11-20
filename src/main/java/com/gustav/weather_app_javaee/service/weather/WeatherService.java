package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface WeatherService {

    List<WeatherEntity> getAllWeather();
    WeatherEntity getWeatherByCity(String city);
    Optional<WeatherEntity> getWeatherById(Long id);
    boolean deleteWeatherById(Long id);
    WeatherEntity saveWeatherData(String cityName, WeatherDTO weatherDTO);
    Optional<Double> getAverageTemperature(String cityName);
    WeatherEntity updateWeather(Long id, WeatherDTO updatedWeather);

}//TODO getWeatherByCity kanske lite onödig?
