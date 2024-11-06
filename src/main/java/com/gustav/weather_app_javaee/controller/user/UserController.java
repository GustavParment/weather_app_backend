package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.exception.UserNotFoundException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.service.user.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/by/{id}")
    public ResponseEntity <?> getUserById(@PathVariable Long id) {
        UserEntity  user = userService.getUserById(id);
            if (user == null) {
                throw new UserNotFoundException("No user found with id: " + id);
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("USER Info retrieved successfully: " + user);

    }

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/add")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        if (userService.findUserByName(userDTO.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("USER CREATED successfully" + userService.addUser(userDTO));

    }

/* TODO
    -Skriva tester
    -Returnera Https. eller Int i status()??
    -Felhantera med Lambda?
*  */
}
