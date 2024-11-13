package com.gustav.weather_app_javaee.controller.user;

import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import com.gustav.weather_app_javaee.authorities.jwt.LoginResponse;
import com.gustav.weather_app_javaee.authorities.userrole.Role;
import com.gustav.weather_app_javaee.authorities.userrole.RoleEnum;
import com.gustav.weather_app_javaee.authorities.userrole.RoleSeeder;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.user.LoginUserDto;
import com.gustav.weather_app_javaee.model.dto.user.RegisterUserDto;
import com.gustav.weather_app_javaee.service.user.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private UserEntity userEntity;
    @Autowired
    private RoleSeeder roleSeeder;

    @BeforeEach
    void setUp() {
        registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("testUser");
        registerUserDto.setFullName("testFullName");
        registerUserDto.setPassword("password123");

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("testUser");
        loginUserDto.setPassword("password123");

        Role role = new Role();
        role.setName(RoleEnum.USER);

        userEntity = new UserEntity();
        userEntity.setEmail("testUser");
        userEntity.setRole(role);



    }

    @Test
    void test_Register_User() throws Exception {
        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testUser")));
    }

    @Test
    void test_Authenticate_User() throws Exception {
        String fakeJwtToken = "fake-jwt-token";

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(userEntity);
        when(jwtService.generateToken(userEntity)).thenReturn(fakeJwtToken);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(fakeJwtToken)))
                .andExpect(jsonPath("$.expiresIn", is(3600)));
    }


}
