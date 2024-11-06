package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.Dao.UserDao;
import com.gustav.weather_app_javaee.authorities.UserRole;
import com.gustav.weather_app_javaee.security.jwt.JwtUtilities;
import com.gustav.weather_app_javaee.model.dto.user.LoginDTO;
import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final GenericConverter converter;
    private final JwtUtilities jwtUtilities;
    private final AuthenticationManager authenticationManager;

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

    @Override
    public String authenticate(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userDao.findUser(authentication.getName());

        return jwtUtilities.generateToken(user.getUsername(),user.getRole());
    }


}
