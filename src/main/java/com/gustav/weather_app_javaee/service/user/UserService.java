package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.model.UserEntity;

import java.util.List;

public interface UserService {
    boolean updatePassword(String username, String password);
    UserEntity getUserById(Long id);
    UserEntity createAdministrator(RegisterUserDto user);
    List<UserEntity> allUsers();


}
// TODO -  Implementera updateUser() i endpoint
