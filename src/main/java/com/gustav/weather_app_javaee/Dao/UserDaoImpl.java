package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
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
    public boolean updatePassword(String username, String password) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setPassword(password);
            userRepository.save(user);

            return true;
        }
        return false;
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public UserEntity findUser(String username) {
        return userRepository
                .findByUsername(username)
                .orElse(null);
    }

}
