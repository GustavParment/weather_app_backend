package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.authorities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

}
