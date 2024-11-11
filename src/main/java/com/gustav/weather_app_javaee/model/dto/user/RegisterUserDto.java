package com.gustav.weather_app_javaee.model.dto.user;


import lombok.Data;


@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;


}