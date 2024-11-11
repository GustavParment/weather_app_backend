package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);


    @Query(value = "SELECT * FROM app_user u " +
            "WHERE u.username " +
            "LIKE CONCAT(?1, '%')",
            nativeQuery = true
    )
    List<User> findByUsernameStartingWith(String prefix);
}
/*TODO
   -Implementera fler querys?
* */