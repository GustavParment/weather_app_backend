package com.gustav.weather_app_javaee.service.user;

import com.gustav.weather_app_javaee.authorities.UserRole;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity addUser(UserDTO dto) {
        UserEntity convertedUser = convertToEntity(dto);

        return userRepository.save(convertedUser);
    }

    @Override
    public UserEntity updateUser(UserDTO user) {
        return null;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return null;
    }

    @Override
    public UserEntity convertToEntity(UserDTO dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole()
                        !=null ? dto.getRole() : UserRole.USER )
                .build();
    }
}
