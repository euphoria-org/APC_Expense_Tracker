package org.expensetracker.controller;

import org.expensetracker.repositories.WalletRepository;
import org.expensetracker.service.UserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailService userDetailService;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    @WithMockUser
    void testAddUser() throws Exception {
        mockMvc.perform(post("/signup")
                .header("username", "testuser")
                .header("password", "testpass")
                .header("wallet_name", "testwallet")
                .header("budget", "1000.00")
                .with(csrf()))
                .andExpect(status().isOk());
        verify(userDetailService, times(1)).addUser("testuser", "testpass");
        verify(walletRepository, times(1)).addWallet("testwallet", "testuser", new BigDecimal("1000.00"), new BigDecimal("1000.00"));
    }
}
