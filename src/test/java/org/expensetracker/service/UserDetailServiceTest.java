package org.expensetracker.service;

import org.expensetracker.model.users;
import org.expensetracker.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    void testLoadUserByUsername_UserExists() {
        users user = new users();
        user.setUsername("serviceUser");
        user.setPassword("servicePass");
        when(userRepository.findByUsername("serviceUser")).thenReturn(user);
        var details = userDetailService.loadUserByUsername("serviceUser");
        assertThat(details.getUsername()).isEqualTo("serviceUser");
        assertThat(details.getPassword()).isEqualTo("servicePass");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("noUser")).thenReturn(null);
        assertThatThrownBy(() -> userDetailService.loadUserByUsername("noUser"))
            .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void testAddUser_NewUser() {
        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("newPass")).thenReturn("hashedPass");
        userDetailService.addUser("newUser", "newPass");
        verify(userRepository).addNewUser("newUser", "hashedPass");
    }

    @Test
    void testAddUser_ExistingUser() {
        users user = new users();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(user);
        userDetailService.addUser("existingUser", "anyPass");
        verify(userRepository, never()).addNewUser(anyString(), anyString());
    }
}

