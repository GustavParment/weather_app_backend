package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.User;

import java.util.List;

public interface UserService {
    User updateUser(UserDTO user);
    User getUserById(Long id);
    User createAdministrator(RegisterUserDto user);
    List<User> allUsers();


}
// TODO -  Implementera updateUser() i endpoint
