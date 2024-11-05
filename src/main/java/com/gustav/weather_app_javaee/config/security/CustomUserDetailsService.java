package com.gustav.weather_app_javaee.config.security;


import com.gustav.weather_app_javaee.Dao.UserDao;
import com.gustav.weather_app_javaee.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findUser(username);
    }
}

//TODO - Skriva test som kollar om loadUserByUsername kastar exception
