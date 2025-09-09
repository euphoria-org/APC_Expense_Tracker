package org.expensetracker.controller;

import org.expensetracker.model.wallets;
import org.expensetracker.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    @WithMockUser(username = "testuser")
    void testGetWalletsByUsername() throws Exception {
        wallets w1 = new wallets();
        w1.setWallet_name("wallet1");
        w1.setUsername("testuser");
        w1.setBalance(new BigDecimal("500.00"));
        w1.setBudget(new BigDecimal("1000.00"));
        wallets w2 = new wallets();
        w2.setWallet_name("wallet2");
        w2.setUsername("testuser");
        w2.setBalance(new BigDecimal("300.00"));
        w2.setBudget(new BigDecimal("800.00"));
        List<wallets> walletsList = Arrays.asList(w1, w2);
        when(walletRepository.getWalletByUsername("testuser")).thenReturn(walletsList);

        mockMvc.perform(get("/getWallets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].wallet_name").value("wallet1"))
                .andExpect(jsonPath("$[1].wallet_name").value("wallet2"));
        verify(walletRepository, times(1)).getWalletByUsername("testuser");
    }

    @Test
    @WithMockUser
    void testChangeBudget() throws Exception {
        BigDecimal newBudget = new BigDecimal("1500.00");
        String username = "testuser";
        mockMvc.perform(post("/changeBudget")
                .header("newBudget", newBudget.toString())
                .header("username", username)
                .with(csrf()))
                .andExpect(status().isOk());
        verify(walletRepository, times(1)).changeBudget(newBudget, username);
    }
}

