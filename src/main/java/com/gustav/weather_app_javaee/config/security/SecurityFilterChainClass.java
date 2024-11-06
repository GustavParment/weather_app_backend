package com.gustav.weather_app_javaee.config.security;

import com.gustav.weather_app_javaee.authorities.UserRole;
import com.gustav.weather_app_javaee.config.security.jwt.JwtAuthenticationEntryPoint;
import com.gustav.weather_app_javaee.config.security.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityFilterChainClass {
    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;
    /*
    Todo
     - Fixa securityFilterChain rätt roll ska kunna göra rätt request
     - Skriva Tester??

    * */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/api/v1/weather/all").permitAll()
                        .requestMatchers("/", "/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/Weather/update/{id}/{city}").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/v1/Weather/delete/{id}").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/v1/user//by/{id}").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/v1/user/update/{id}").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/v1/average-temp/{cityName}").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/v1/user/add").hasRole(UserRole.GUEST.name())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(
                        authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}