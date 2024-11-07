package com.gustav.weather_app_javaee.controller.weather;

import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.weather.WeatherApiService;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/fetch")
public class WeatherApiController {

    private final WeatherApiService weatherApiService;
    private final WeatherService weatherService;

    public WeatherApiController(WeatherApiService weatherApiService, WeatherService weatherService) {
        this.weatherApiService = weatherApiService;
        this.weatherService = weatherService;
    }


    @GetMapping("/{city}")
    @RateLimiter(name = "rateLimiter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<WeatherDTO>> fetchWeatherByName(@PathVariable String city) {
        return weatherApiService.getWeatherFromExternalApi(city)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                .doOnError(error -> System.out.println("Error fetching weather data: " + error.getMessage()));
    }

    //TODO-Maybe change to GET since GUESt are using this


    @PostMapping("/save/{city}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<WeatherDTO>> fetchWeatherData(@PathVariable String city) {
        System.out.println("Attempting to access save endpoint as ADMIN...");

        return weatherApiService.getWeatherFromExternalApi(city)
                .doOnSuccess(weatherDTO -> {
                    weatherService.addWeather(city, weatherDTO);
                })
                .map(weatherDTO -> ResponseEntity.status(HttpStatus.CREATED).body(weatherDTO))
                .doOnError(error -> System.out.println("Error fetching weather data: " + error.getMessage()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()); // Returnerar 500 vid fel
    }
    //TODO-Maybe change to GET Since GUEST are using this

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}/{city}")
    public Mono<ResponseEntity<WeatherEntity>> updateWeather(@PathVariable Long id, @PathVariable String city) {
        return weatherApiService.getWeatherFromExternalApi(city)
                .flatMap(weatherDTO -> {
                    WeatherEntity updatedWeather = weatherService.updateWeather(id, weatherDTO);

                    if (updatedWeather == null) {
                        return Mono.just(
                                ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .<WeatherEntity>build()
                        );
                    }
                    return Mono.just(ResponseEntity.ok(updatedWeather));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).<WeatherEntity>build())
                .doOnError(error -> System.out.println("Error updating weather data: " + error.getMessage()));
    }

}
