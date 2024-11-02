package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.model.dto.AuthResponseDTO;
import com.gustav.weather_app_javaee.model.dto.LoginRequestDTO;
import com.gustav.weather_app_javaee.service.login.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController()
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO authResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
    }





}
