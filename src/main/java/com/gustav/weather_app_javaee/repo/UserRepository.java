package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity , Long> {
    Optional<UserEntity> findByUsername(String username);
}
