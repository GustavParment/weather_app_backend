package com.gustav.weather_app_javaee.component.converter;

import com.gustav.weather_app_javaee.authorities.userrole.Role;
import com.gustav.weather_app_javaee.authorities.userrole.RoleEnum;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.repo.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class GenericConverter {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    /*TODO
       -Skriva tester
    * */

    public UserEntity convertUserToEntity(RegisterUserDto dto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) return null;

        UserEntity userEntity = new UserEntity();
                userEntity.setFullName((dto.getFullName()));
                userEntity.setEmail(dto.getEmail());
                userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
                userEntity.setRole(optionalRole.get());


        return userEntity;
    }

    public UserEntity convertToAdminEntity(RegisterUserDto dto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) return null;

        UserEntity adminUserEntity = new UserEntity();
        adminUserEntity.setFullName(dto.getFullName());
        adminUserEntity.setEmail(dto.getEmail());
        adminUserEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        adminUserEntity.setRole(optionalRole.get());

        return adminUserEntity;
    }

    public WeatherEntity convertToWeatherEntity(String cityName, WeatherDTO dto) {
        return WeatherEntity.builder()
                .city_name(cityName.toUpperCase())
                .country_code(dto.getCountryCode())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .stateCode(dto.getStateCode())
                .timezone(dto.getTimezone())
                .temp(dto.getData()
                        .isEmpty() ? 0 : dto.getData()
                        .getFirst()
                        .getTemp())
                .clouds(dto.getData()
                        .isEmpty() ? 0 : dto.getData()
                        .getFirst()
                        .getClouds())
                .maxTemp(dto.getData()
                        .isEmpty() ? 0 : dto.getData()
                        .getFirst()
                        .getMaxTemp())
                .minTemp(dto.getData()
                        .isEmpty() ? 0 : dto.getData()
                        .getFirst()
                        .getMinTemp()
                )
                .weatherDescription(
                        dto.getData().isEmpty() ? "N/A" : dto.getData()
                                .getFirst()
                                .getWeather()
                                .getDescription()
                )
                .datetime(dto.getData().isEmpty() ? "N/A" : dto.getData().getFirst().getDatetime())
                .build();
    }
}
