package com.gustav.weather_app_javaee.controller.weather;

import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllWeatherFromDb() {
        try {
            if (weatherService.getAllWeather().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("No weather found");
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(weatherService.getAllWeather());
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error" + e.getMessage());
        }
    }

    @GetMapping("/average-temperature/{cityName}")
    public ResponseEntity<String> getAverageTemperature(@PathVariable String cityName) {
        return weatherService.getAverageTemperature(cityName)
                .map(avgTemp -> ResponseEntity.ok(
                        "Average temperature for "
                                + cityName + ": "
                                + avgTemp + "°C"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No weather data found for city: " + cityName));
    }

    @DeleteMapping("/{id}")
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

    @GetMapping("/{id}")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<WeatherEntity> getWeatherById(@PathVariable Long id) {
        Optional<WeatherEntity> weatherEntityOptional = weatherService.getWeatherById(id);

        return weatherEntityOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
}




