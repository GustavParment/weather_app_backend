package com.gustav.weather_app_javaee.config.webconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfigClass {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
