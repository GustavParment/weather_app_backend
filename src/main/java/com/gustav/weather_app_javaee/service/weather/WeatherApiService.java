package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherApiService {
    private final WebClient webClient;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    private String buildWeatherUrl(String city) {
        return String.format("https://api.weatherbit.io/v2.0/forecast/daily?city=%s&key=%s", city, apiKey);
    }

    public Mono<WeatherDTO> getWeatherFromExternalApi(String city) {
        String uri = buildWeatherUrl(city);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    String errorMessage = String.format("Error fetching weather data: %s", response.statusCode());
                    return Mono.error(new RuntimeException(errorMessage));
                })
                .bodyToMono(WeatherDTO.class)
                .doOnError(e -> System.err.println("Error during WebClient call: " + e.getMessage()));
    }
}
