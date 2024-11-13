package com.gustav.weather_app_javaee.service.user;


import com.gustav.weather_app_javaee.Dao.UserDao;
import com.gustav.weather_app_javaee.component.converter.GenericConverter;
import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.user.LoginUserDto;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.repo.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationService {
    private final UserDao userDao;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final GenericConverter converter;


    public UserEntity signup(RegisterUserDto input) {
        log.info("signup");

        UserEntity userEntity = converter.convertUserToEntity(input);

        return userDao.save(userEntity);
    }

    public UserEntity authenticate(LoginUserDto input) {
        log.info("authenticate");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userDao.findByEmail(input.getEmail());

    }
}

