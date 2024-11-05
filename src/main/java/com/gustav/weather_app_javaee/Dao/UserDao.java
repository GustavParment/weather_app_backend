package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<UserEntity> findUserStartingWith(String prefix);
    boolean updatePassword(String username, String password);
    UserEntity addUser(UserEntity user);
    UserEntity updateUser(UserEntity user);
    UserEntity getUserById(Long id);
    UserEntity findUser(String username);

}
