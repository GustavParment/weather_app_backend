package com.gustav.weather_app_javaee.service.login.logout;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class LogoutService {
    private final Set<String> blackListedToken = new HashSet<>();

    public void addToBlackList(String token) {
        blackListedToken.add(token);
    }

    public boolean isTokenBlackListed(String token) {
        return blackListedToken.contains(token);
    }
}
