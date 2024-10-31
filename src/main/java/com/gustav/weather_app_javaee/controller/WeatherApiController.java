package com.gustav.weather_app_javaee.controller;

import com.gustav.weather_app_javaee.dto.WeatherDTO;
import com.gustav.weather_app_javaee.service.WeatherApiService;
import com.gustav.weather_app_javaee.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class WeatherApiController {

    private final WeatherApiService weatherApiService;
    private final WeatherService weatherService;

    public WeatherApiController(WeatherApiService weatherApiService, WeatherService weatherService) {
        this.weatherApiService = weatherApiService;
        this.weatherService = weatherService;
    }

    @GetMapping("/fetch/{city}")
    public Mono<WeatherDTO> fetchWeatherData(@PathVariable String city) {
        return weatherApiService.getWeatherFromExternalApi(city)
                .doOnSuccess(weatherDTO -> weatherService.addWeather(city, weatherDTO))
                .doOnError(error -> System.out.println("Error fetching weather data: " + error.getMessage()));
    }
}
