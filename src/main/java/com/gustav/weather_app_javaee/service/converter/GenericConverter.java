package com.gustav.weather_app_javaee.service.converter;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.model.dto.WeatherDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GenericConverter {
    private final PasswordEncoder encoder;

    public UserEntity convertToUserEntity(UserDTO dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }

    public WeatherEntity convertToWeatherEntity(String cityName, WeatherDTO dto) {
        return WeatherEntity.builder()
                .city_name(cityName.toUpperCase())
                .country_code(dto.getCountryCode())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .stateCode(dto.getStateCode())
                .timezone(dto.getTimezone())
                .temp(dto.getData().isEmpty() ? 0 : dto.getData().getFirst().getTemp())
                .clouds(dto.getData().isEmpty() ? 0 : dto.getData().getFirst().getClouds())
                .maxTemp(dto.getData().isEmpty() ? 0 : dto.getData().getFirst().getMaxTemp())
                .minTemp(dto.getData().isEmpty() ? 0 : dto.getData().getFirst().getMinTemp())
                .weatherDescription(dto.getData().isEmpty() ? "N/A" : dto.getData().getFirst().getWeather().getDescription())
                .datetime(dto.getData().isEmpty() ? "N/A" : dto.getData().getFirst().getDatetime())
                .build();
    }
}
