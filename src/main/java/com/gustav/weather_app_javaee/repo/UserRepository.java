package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity , Long> {
}
