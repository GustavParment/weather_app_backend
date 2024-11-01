package com.gustav.weather_app_javaee.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gustav.weather_app_javaee.authorities.UserPermission.*;

public enum UserRole {

    GUEST(GET),
    USER(GET, PUT),
    ADMIN(GET, POST, PUT, DELETE);

    private final List<String> permission;

    UserRole(UserPermission... permissionList) {
        this.permission = Arrays.stream(permissionList)
                .map(UserPermission::getPermission)
                .toList();
    }

    public List<String> getListOfPermissions() {
        return permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        simpleGrantedAuthorityList.add(
                new SimpleGrantedAuthority("ROLE_" + this.name()));
        simpleGrantedAuthorityList.addAll(
                getListOfPermissions()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());

        return simpleGrantedAuthorityList;
    }

}
