package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.Dao.UserDao;


import com.gustav.weather_app_javaee.authorities.userrole.Role;
import com.gustav.weather_app_javaee.authorities.userrole.RoleEnum;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.User;
import com.gustav.weather_app_javaee.repo.RoleRepository;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import lombok.AllArgsConstructor;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final GenericConverter converter;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    /*TODO
       -Implementera RoleDao
       -Skriva Tester
       - Bryta ut kod
    * */


    @Override
    public User updateUser(UserDTO dto) {

        User convertedUser = converter.convertToUserEntity(dto);
        return userDao.updateUser(convertedUser);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        if (optionalRole.isEmpty()) {
            return null;
        }

        var adminUser = new User();
                adminUser.setFullName(input.getFullName());
                adminUser.setEmail(input.getEmail());
                adminUser.setPassword(passwordEncoder.encode(input.getPassword()));
                adminUser.setRole(optionalRole.get());

        return userDao.createAdmin(adminUser);
    }

    @Override
    public List<User> allUsers() {
        return userDao.findAll();
    }
}
