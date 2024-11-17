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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    private final Set<String> tokenBlacklist = new HashSet<>();

    /*TODO
       -Skriva Tester
       -Kolla över felhantering
    * */
    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService,
            UserDetailsService userDetailsService
    )
    {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity registeredUserEntity = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUserEntity);
    }

//    @RateLimiter(name = "rateLimiter")
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(
//            @RequestBody LoginUserDto loginUserDto
//    )
//    {
//        UserEntity authenticatedUserEntity = authenticationService.authenticate(loginUserDto);
//
//        String jwtToken = jwtService.generateToken(authenticatedUserEntity);
//
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setToken(jwtToken);
//        loginResponse.setExpiresIn(jwtService.getExpirationTime());
//
//        return ResponseEntity.ok(loginResponse);
//    }
@RateLimiter(name = "rateLimiter")
@PostMapping("/login")
public ResponseEntity<LoginResponse> authenticate(
        @RequestBody LoginUserDto loginUserDto
)
{
    UserEntity authenticatedUserEntity = authenticationService.authenticate(loginUserDto);

    // Skapa token och inkludera roller i payloaden
    String jwtToken = jwtService.generateToken(authenticatedUserEntity);

    // Skapa och skicka svar
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(jwtToken);
    loginResponse.setExpiresIn(jwtService.getExpirationTime());

    return ResponseEntity.ok(loginResponse);
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7); // Ta bort "Bearer "
        String username = jwtService.extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails)) {
            tokenBlacklist.add(token);
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}








