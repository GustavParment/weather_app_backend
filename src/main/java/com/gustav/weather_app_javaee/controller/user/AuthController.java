package com.gustav.weather_app_javaee.controller.user;


import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.security.jwt.AuthenticationResponse;
import com.gustav.weather_app_javaee.security.jwt.AuthenticationRequest;
import com.gustav.weather_app_javaee.security.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin("*")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse createToken(@RequestBody AuthenticationRequest request) {
        log.info("createToken(-)");

        // Authenticate the user, this will return a UserDetails object
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Cast the UserDetails object to UserEntity
        if (userDetails instanceof UserEntity userEntity) {
            String role = userEntity.getRole().name();

            String jwtToken = jwtUtil.generateToken(request.getUsername(), role);

            return new AuthenticationResponse(jwtToken);
        } else {
            throw new RuntimeException("User not found or invalid UserDetails object.");
        }
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> getUserRoleFromToken(@RequestHeader("Authorization") String token) {
        // Kontrollera att token är i rätt format "Bearer <token>"
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Ta bort "Bearer " från början
        } else {
            return ResponseEntity.badRequest().body("Invalid token format.");
        }

        // Validera token och extrahera claims
        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body("Token is expired.");
        }

        // Extrahera claims och roll från token
        Claims claims = jwtUtil.extractClaims(token);
        String role = claims.get("role", String.class); // Extrahera roll

        // Returnera roll i responsen
        return ResponseEntity.ok().body("User role: " + role);
    }

}
