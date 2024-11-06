package com.gustav.weather_app_javaee.controller.user;



import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.exception.UserNotFoundException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.model.dto.UserDTO;
import com.gustav.weather_app_javaee.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_Should_Return_User_By_Id(){
        Long userId = 1L;
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("Benny");


        when(userService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.getUserById(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USER Info retrieved successfully: " + mockUser,
                Objects.requireNonNull(response.getBody()).toString());
        verify(userService,times(1)).getUserById(userId);

    }

    @Test
    void getUserById_Should_Throw_Exception_When_User_Does_Not_Exist(){
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userController.getUserById(userId));

        assertEquals("No user found with id: " + userId, exception.getMessage());

        verify(userService,times(1)).getUserById(userId);
    }

    @Test
    void createUser_Should_Create_User_And_Save_To_Database(){
        UserDTO mockUserDto = new UserDTO();
        mockUserDto.setUsername("Frida");

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(mockUserDto.getUsername());

        when(userService.addUser(mockUserDto)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.createUser(mockUserDto);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains(mockUserDto.getUsername()));

        verify(userService,times(1)).addUser(mockUserDto);
    }

    @Test
    void creatUser_Should_Throw_Exception_When_User_Already_Exist(){
        UserDTO mockUserDto = new UserDTO();
        mockUserDto.setUsername("Frida");
        when(userService.findUserByName(mockUserDto.getUsername())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userController.createUser(mockUserDto));

        assertEquals("Username already exists", exception.getMessage());

    }

}