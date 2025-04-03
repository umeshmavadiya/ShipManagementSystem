package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.dto.UserDTO;
import com.example.ShipManagementSystem.entity.RouteEntity;
import com.example.ShipManagementSystem.entity.UserEntity;
import com.example.ShipManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private  UserService  userService;

    private UserEntity userEntity;
    private UserDTO userDTO;


    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password123");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setPassword("encodedPassword");
    }

    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        List<UserEntity> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser() {
        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}