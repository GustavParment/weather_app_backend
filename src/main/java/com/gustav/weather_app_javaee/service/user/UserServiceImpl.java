package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.Dao.UserDao;


import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.component.converter.GenericConverter;
import lombok.AllArgsConstructor;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final GenericConverter converter;
    private final BCryptPasswordEncoder passwordEncoder;
    /*TODO
       -Implementera RoleDao
       -Skriva Tester
       * */

    @Override
    public boolean updatePassword(String username, String password) {
        return userDao.updatePassword(username, passwordEncoder.encode(password));
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public UserEntity createAdministrator(RegisterUserDto input) {
        UserEntity adminUserEntity = converter.convertToAdminEntity(input);

        return userDao.createAdmin(adminUserEntity);
    }

    @Override
    public List<UserEntity> allUsers() {
        return userDao.findAll();
    }
}
