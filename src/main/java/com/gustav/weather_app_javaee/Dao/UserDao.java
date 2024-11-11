package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.User;

import java.util.List;

public interface UserDao {
    List<User> findUserStartingWith(String prefix);
    boolean updatePassword(String username, String password);
    User addUser(User user);
    User updateUser(User user);
    User getUserById(Long id);
    List<User> findAll();
    User createAdmin(User user);

}
