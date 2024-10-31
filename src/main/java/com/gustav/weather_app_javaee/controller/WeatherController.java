package com.gustav.weather_app_javaee.controller;

import com.gustav.weather_app_javaee.dto.WeatherDTO;
import com.gustav.weather_app_javaee.dto.WeatherDataList;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/all")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<?> getAllWeatherFromDb() {
        try {
            if (weatherService.getAllWeather().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("No weather found");
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            "<---Successfully retrieved all the weather from DB---> "
                                    + weatherService.getAllWeather()
                    );
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error" + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<?> updateWeather(
            @PathVariable Long id,
            @RequestBody WeatherDTO weatherDTO) {
        try {
            WeatherEntity updatedWeather = weatherService.updateWeather(id, weatherDTO);

            if (updatedWeather == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No weather data found for given city");
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(weatherService.updateWeather(id, weatherDTO));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error" + e.getMessage());

        }


    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "myRateLimiter")
    public ResponseEntity<String> deleteWeatherById(@PathVariable Long id) {
        boolean deleted = weatherService.deleteWeatherById(id);

        try {
            if (deleted) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("Deleted weather with Id " + id + " successfully");

            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Weather with Id " + id + " not found");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error" + e.getMessage());

        }
    }
}




