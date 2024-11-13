package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import com.gustav.weather_app_javaee.authorities.userrole.Role;
import com.gustav.weather_app_javaee.authorities.userrole.RoleEnum;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;


@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "SuperAdmin", roles = "SUPER_ADMIN")
    void test_Create_Admin_With_SuperAdmin_Role() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setFullName("John Doe");
        registerUserDto.setEmail("Admin@gustav.com");
        registerUserDto.setPassword("123");

        UserEntity createAdminUser = new UserEntity();

        createAdminUser.setFullName("John Doe");
        createAdminUser.setEmail("Admin@gustav.com");
        createAdminUser.setPassword("123");



        when(userService.createAdministrator(Mockito.any(RegisterUserDto.class))).thenReturn(createAdminUser);

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerUserDto)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.username").value("adminUser"));
    }


}