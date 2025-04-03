package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.entity.UserEntity;
import com.example.ShipManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("john@example.com");
        userEntity.setPassword("password123");
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = customUserDetailService.loadUserByUsername("john@example.com");

        assertNotNull(userDetails);
        assertEquals("john@example.com", userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customUserDetailService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
    }