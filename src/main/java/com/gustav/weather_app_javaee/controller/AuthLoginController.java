package com.gustav.weather_app_javaee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/auth")
public class AuthLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/custom-login")
    public String login() {
        return "Welcome to Custom Login Page";
    }

}
