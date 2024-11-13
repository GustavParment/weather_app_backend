package com.gustav.weather_app_javaee.controller.user;


import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.service.user.AuthenticationService;
import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import com.gustav.weather_app_javaee.authorities.jwt.LoginResponse;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.user.LoginUserDto;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService
    )
    {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity registeredUserEntity = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUserEntity);
    }

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserEntity authenticatedUserEntity = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUserEntity);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}








