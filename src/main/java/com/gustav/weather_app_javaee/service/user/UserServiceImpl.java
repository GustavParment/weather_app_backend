package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.Dao.UserDao;


import com.gustav.weather_app_javaee.authorities.Role;
import com.gustav.weather_app_javaee.model.dto.user.UserDTO;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.repo.RoleRepository;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final GenericConverter converter;
    private final RoleRepository roleRepository;


    @Override
    public UserEntity addUser(UserDTO dto) {
        // Hämta eller skapa roller
        Set<Role> roles = new HashSet<>();
        for (Role role : dto.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName());
            if (existingRole != null) {
                roles.add(existingRole);  // Lägg till den befintliga rollen
            } else {
                // Om rollen inte finns, skapa och spara den
                roleRepository.save(role);
                roles.add(role);
            }
        }

        // Skapa användaren med de sparade rollerna
        UserEntity convertedUser = converter.convertToUserEntity(dto);
        convertedUser.setRoles(roles);

        // Spara användaren
        return userDao.addUser(convertedUser);
    }

    @Override
    public UserEntity updateUser(UserDTO dto) {
        // Samma sak här, hämta eller skapa roller
        Set<Role> roles = new HashSet<>();
        for (Role role : dto.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName());
            if (existingRole != null) {
                roles.add(existingRole);
            } else {
                roleRepository.save(role);
                roles.add(role);
            }
        }

        // Uppdatera användaren med de sparade rollerna
        UserEntity convertedUser = converter.convertToUserEntity(dto);
        convertedUser.setRoles(roles);

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



}
