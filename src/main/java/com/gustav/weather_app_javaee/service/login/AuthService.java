package com.gustav.weather_app_javaee.service.login;

import com.gustav.weather_app_javaee.model.dto.AuthResponseDTO;
import com.gustav.weather_app_javaee.model.dto.LoginRequestDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    void logout(String token);
}
