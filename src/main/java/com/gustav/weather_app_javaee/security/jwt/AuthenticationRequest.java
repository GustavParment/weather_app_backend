package com.gustav.weather_app_javaee.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
    private String role;
}
