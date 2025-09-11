package org.expensetracker.service;

import org.expensetracker.model.users;
import org.expensetracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailService userDetailService;

    private users testUser;

    @BeforeEach
    void setUp() {
        testUser = new users();
        testUser.setUsername("testuser");
        testUser.setPassword("hashedpassword");
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotExists() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername("nonexistent");
        });
        
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void addUser_ShouldCreateNewUser_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");

        // Act
        userDetailService.addUser("newuser", "password");

        // Assert
        verify(userRepository).findByUsername("newuser");
        verify(passwordEncoder).encode("password");
        verify(userRepository).addNewUser("newuser", "hashedpassword");
    }

    @Test
    void addUser_ShouldNotCreateUser_WhenUserAlreadyExists() {
        // Arrange
        when(userRepository.findByUsername("existinguser")).thenReturn(testUser);

        // Act
        userDetailService.addUser("existinguser", "password");

        // Assert
        verify(userRepository).findByUsername("existinguser");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).addNewUser(any(), any());
    }
}
