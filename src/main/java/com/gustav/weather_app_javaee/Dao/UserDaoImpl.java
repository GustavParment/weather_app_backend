package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.model.User;
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
    public List<User> findUserStartingWith(String prefix) {
        return userRepository.findByUsernameStartingWith(prefix);
    }

    @Override
    @Transactional
    public boolean updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setPassword(password);
            userRepository.save(user);

            return true;
        }
        return false;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());

    }


    @Override
    public User createAdmin(User admin) {
        return userRepository.save(admin);
    }

}
