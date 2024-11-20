package com.gustav.weather_app_javaee.Dao.user;

import com.gustav.weather_app_javaee.model.UserEntity;

import java.util.List;

public interface UserDao {
    List<UserEntity> findUserStartingWith(String prefix);
    boolean updatePassword(String username, String password);
    UserEntity save (UserEntity userEntity);
    UserEntity findByEmail(String email);
    UserEntity updateUser(UserEntity userEntity);
    UserEntity getUserById(Long id);
    List<UserEntity> findAll();
    UserEntity createAdmin(UserEntity userEntity);


}
