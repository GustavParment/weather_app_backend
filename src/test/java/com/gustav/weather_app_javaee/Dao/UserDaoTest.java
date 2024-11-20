package com.gustav.weather_app_javaee.Dao;

import com.gustav.weather_app_javaee.Dao.user.UserDaoImpl;
import com.gustav.weather_app_javaee.exception.UserAlreadyExistsException;
import com.gustav.weather_app_javaee.exception.UserNotFoundException;
import com.gustav.weather_app_javaee.model.UserEntity;
import com.gustav.weather_app_javaee.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserDaoTest {
    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserDaoImpl dao;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setEmail("existing@user.com");
    }
    @Test
    void test_Should_Throw_Exception_When_User_Exists_In_Database() {
        when(repo.existsByEmail(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class, () ->
                        dao.save(userEntity)
        );

        assertEquals("User with this email already exists", exception.getMessage());
    }

    @Test
    void test_Should_Throw_Exception_When_User_Does_Not_ExistById() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class, () -> dao.getUserById(1L)
                );

        assertEquals("User with this id does not exist", exception.getMessage());
    }

    @Test
    void test_Should_Throw_Exception_When_User_Does_Not_ExistByEmail() {
        when(repo.existsByEmail(anyString())).thenReturn(false);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class, () ->
                        dao.findByEmail(userEntity.getEmail())
        );
        assertEquals("User with this email does not exist", exception.getMessage());
    }



}