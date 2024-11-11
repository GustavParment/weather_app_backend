package com.gustav.weather_app_javaee.service.converter;

import com.gustav.weather_app_javaee.model.User;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GenericConverter {
    private final PasswordEncoder encoder;
    /*TODO
       -Implementera convertToUserEntity i servicen?
    * */

    public User convertToUserEntity(UserDTO dto) {
        User user = new User();
                user.setFullName((dto.getFullName()));
                user.setEmail(dto.getEmail());
                user .setPassword(encoder.encode(dto.getPassword()));
                user.setCreatedAt(dto.getCreatedAt());
                user.setUpdatedAt(dto.getUpdatedAt());

        return user;
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
