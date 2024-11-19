package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserEntity> createAdministrator(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity createdAdmin = userService.createAdministrator(registerUserDto);

        return ResponseEntity.ok(createdAdmin);
    }
}