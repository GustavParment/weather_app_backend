package com.gustav.weather_app_javaee.security;

import com.gustav.weather_app_javaee.authorities.jwt.CustomUserDetailsService;
import com.gustav.weather_app_javaee.authorities.jwt.JwtAuthenticationEntryPoint;
import com.gustav.weather_app_javaee.authorities.jwt.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityFilterChainClass(UserDetailsService userDetailsService,
                                    JwtAuthenticationEntryPoint authenticationEntryPoint,
                                    JwtAuthenticationFilter authenticationFilter
    )
    {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/admin").hasAuthority("ADMIN")
                                .requestMatchers("/api/user").hasAuthority("USER")
                                .requestMatchers( "/api/v1/weather/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/fetch/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/user/add").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()
//
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(
                        authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


}
