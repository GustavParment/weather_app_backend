package com.gustav.weather_app_javaee.authorities.jwt;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;



}