package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.UserEntity;

public interface UserService {
    UserEntity addUser(UserDTO user);
    UserEntity updateUser(UserDTO user);
    UserEntity getUserById(Long id);
    boolean findUserByName(String name);


}
// TODO -  Implementera updateUser() i endpoint
