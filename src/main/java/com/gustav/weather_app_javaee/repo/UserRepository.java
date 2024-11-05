package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.model.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity , Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query(value = "SELECT * FROM app_user u " +
            "WHERE u.username " +
            "LIKE CONCAT(?1, '%')",
            nativeQuery = true
    )
    List<UserEntity> findByUsernameStartingWith(String prefix);
}