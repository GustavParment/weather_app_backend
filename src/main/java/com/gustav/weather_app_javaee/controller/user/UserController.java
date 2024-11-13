package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.exception.UserNotFoundException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.service.user.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @RateLimiter(name = "rateLimiter")
    @GetMapping("/by/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userService.getUserById(id);
        if (userEntity == null) {
            throw new UserNotFoundException("No user found with id: " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("USER Info retrieved successfully: " + userEntity);

    }
}




/* TODO
    -Skriva tester
    -Lägga till fler Endpoints PUT,POST?

*  */

