package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Repository
public class UserDaoImpl implements UserDao{
    private final UserRepository userRepository;



    @Override
    public List<UserEntity> findUserStartingWith(String prefix) {
        return userRepository.findByUsernameStartingWith(prefix);
    }
}
