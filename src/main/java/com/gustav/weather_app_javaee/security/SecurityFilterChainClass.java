package com.gustav.weather_app_javaee.security;

import com.gustav.weather_app_javaee.authorities.UserRole;
import com.gustav.weather_app_javaee.security.jwt.CustomUserDetailsService;
import com.gustav.weather_app_javaee.security.jwt.JwtRequestFilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityFilterChainClass {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityFilterChainClass(
            CustomUserDetailsService customUserDetailsService,
            JwtRequestFilter jwtRequestFilter
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


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
                        .requestMatchers("/validate").permitAll()
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/api/v1/Weather/update/{id}/{city}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/Weather/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/by/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/v1/fetch/save/{city}").hasRole("ADMIN")

                        .requestMatchers("/api/v1/user/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/v1/average-temp/{cityName}").hasRole("USER")
                        .requestMatchers("/api/v1/user/add").hasRole("GUEST")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }
}