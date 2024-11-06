package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.model.dto.user.AuthResponseDTO;
import com.gustav.weather_app_javaee.model.dto.user.LoginDTO;
import com.gustav.weather_app_javaee.service.login.AuthService;
import com.gustav.weather_app_javaee.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.authenticate(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            authService.logout(token);
            return ResponseEntity.ok("Successfully logged out");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
