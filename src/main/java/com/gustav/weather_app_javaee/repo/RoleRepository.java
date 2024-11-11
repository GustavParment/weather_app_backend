package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.authorities.userrole.Role;
import com.gustav.weather_app_javaee.authorities.userrole.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}