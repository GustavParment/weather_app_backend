package com.gustav.weather_app_javaee.controller.weather;

import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fetch")
public class WeatherApiController {
    private final WebClient webClient;
    private final JwtService jwtService;
    private final WeatherService weatherService;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherApiController(WebClient.Builder webClient,
                                JwtService jwtService,
                                WeatherService weatherService
    )
    {
        this.webClient = webClient.baseUrl(apiUrl).build();
        this.jwtService = jwtService;
        this.weatherService = weatherService;
    }

    private String buildWeatherUrl(String city) {
        return String.format("https://api.weatherbit.io/v2.0/forecast/daily?city=%s&key=%s", city, apiKey);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        return jwtService.extractJwtFromRequest(request);
    }

    @RateLimiter(name ="rateLimiter")
    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public Mono<ResponseEntity<WeatherDTO>> fetchWeatherByCity(@PathVariable String city, HttpServletRequest request) {
        String jwtToken = getJwtTokenFromRequest(request);

        if (jwtToken == null || jwtToken.isEmpty()) {
            return Mono.just(ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null));
        }

        return webClient
                .get()
                .uri(buildWeatherUrl(city))
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    String errorMessage = String.format("Error fetching weather data: %s", response.statusCode());
                    return Mono.error(new RuntimeException(errorMessage));
                })

                .bodyToMono(WeatherDTO.class)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                .doOnError(e -> System.err.println("Error during WebClient call: " + e.getMessage()));
    }

    @RateLimiter(name ="rateLimiter")
    @PostMapping("/save/{city}")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'SUPER_ADMIN')")
    public Mono<ResponseEntity<WeatherDTO>> saveWeatherData(@PathVariable String city, HttpServletRequest request) {
        String jwtToken = getJwtTokenFromRequest(request);

        if (jwtToken == null || jwtToken.isEmpty()) {
            return Mono.just(ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null));
        }

        return webClient
                .get()
                .uri(buildWeatherUrl(city))
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    String errorMessage = String.format("Error fetching weather data: %s", response.statusCode());
                    return Mono.error(new RuntimeException(errorMessage));
                })
                .bodyToMono(WeatherDTO.class)
                .map(weatherDTO -> {
                    weatherService.saveWeatherData(city, weatherDTO);

                    return ResponseEntity.status(HttpStatus.CREATED).body(weatherDTO);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                .doOnError(e -> System.err.println("Error saving weather data: " + e.getMessage()));
    }

    @RateLimiter(name = "rateLimiter")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'SUPER_ADMIN')")
    public Mono<ResponseEntity<WeatherEntity>> updateWeatherData(
            @PathVariable Long id,
            @PathVariable WeatherDTO updatedWeatherDTO,
            HttpServletRequest request)
    {
        String jwtToken = getJwtTokenFromRequest(request);

        if (jwtToken == null || jwtToken.isEmpty()) {
            return Mono.just(
                    ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(null)
            );
        }

        Optional<WeatherEntity> optionalWeatherEntity = weatherService.getWeatherById(id);

        if (optionalWeatherEntity.isEmpty()) {
            return Mono.just(
                    ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(null)
            );
        }
        WeatherEntity weatherEntity = optionalWeatherEntity.get();

        return webClient
                .get()
                .uri(buildWeatherUrl(weatherEntity.getCity_name()))
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(WeatherDTO.class)
                .flatMap(
                        dto -> {

                            WeatherEntity updateWeatherW =
                                    weatherService.updateWeather(id,updatedWeatherDTO);
                            return Mono.just(
                                    ResponseEntity
                                            .status(HttpStatus.CREATED)
                                            .body(updateWeatherW));
                        })
                .onErrorResume(e ->{
                    System.out.println("Error updating weather data: " + e.getMessage());
                    return Mono.just(
                            ResponseEntity
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(null));
                });
    }
}
