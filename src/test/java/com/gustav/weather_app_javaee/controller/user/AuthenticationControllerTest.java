package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import com.gustav.weather_app_javaee.service.user.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationControllerTest {
    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {

    }





}