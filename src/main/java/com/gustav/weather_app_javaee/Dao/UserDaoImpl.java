package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.exception.UserNotFoundException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class UserDaoImpl implements UserDao{
    private final UserRepository userRepository;

    @Override
    public List<UserEntity> findUserStartingWith(String prefix) {
        return userRepository.findByUsernameStartingWith(prefix);
    }

    @Override
    @Transactional
    public boolean updatePassword(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity != null) {
            userEntity.setPassword(password);
            userRepository.save(userEntity);

            return true;
        }
        return false;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        if (userRepository.existsByEmail(userEntity.getEmail())){
            throw new UserAlreadyExistsException
                    ("User with this email already exists");

        }
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException
                                ("User with this email does not exist")
                );
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException
                                ("User with this id does not exist")
                        );
    }

    @Override
    public List<UserEntity> findAll() {
        return new ArrayList<>(userRepository.findAll());

    }

    @Override
    public UserEntity createAdmin(UserEntity admin) {
        return userRepository.save(admin);
    }

}
