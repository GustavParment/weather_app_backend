package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.Dao.UserDao;


import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final GenericConverter converter;


    @Override
    public UserEntity addUser(UserDTO dto) {
        UserEntity convertedUser = converter.convertToUserEntity(dto);

        return userDao.addUser(convertedUser);
    }

    @Override
    public UserEntity updateUser(UserDTO dto) {
        UserEntity convertedUser = converter.convertToUserEntity(dto);
        return userDao.updateUser(convertedUser);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public boolean findUserByName(String name) {
        return userDao.findUser(name) != null;
    }



}
