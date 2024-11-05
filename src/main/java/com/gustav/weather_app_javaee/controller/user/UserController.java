package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.service.user.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/by/{id}")
    public ResponseEntity <?> getUserById(@PathVariable Long id) {
        try {
            UserEntity  user = userService.getUserById(id);
            if (user == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(
                            "USER Info retrieved successfully: "
                                    + userService.getUserById(id)
                    );

        }catch(Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Internal server error: " + e.getMessage());

        }
    }

    @RateLimiter(name = "rateLimiter")
    @PostMapping("/add")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity
                    .status(201)
                    .body("User successfully added " + userService.addUser(userDTO));

        }catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Internal Server Error" + e.getMessage());
        }

    }

/* TODO
    -Skriva tester
    -Returnera Https. eller Int i status()??
    -Felhantera med Lambda?
*  */
}
