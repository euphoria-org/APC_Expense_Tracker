package org.expensetracker.controller;

import org.expensetracker.model.wallets;
import org.expensetracker.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WalletController walletController;

    private wallets testWallet;

    @BeforeEach
    void setUp() {
        testWallet = new wallets();
        testWallet.setWallet_name("testwallet");
        testWallet.setUsername("testuser");
        testWallet.setBalanace(new BigDecimal("1000.00"));
        testWallet.setBudget(new BigDecimal("1500.00"));
    }

    @Test
    void getWalletsByUsername_ShouldReturnWalletList_WhenCalled() {
        // Arrange
        List<wallets> expectedWallets = Arrays.asList(testWallet);
        when(authentication.getName()).thenReturn("testuser");
        when(walletRepository.getWalletByUsername("testuser")).thenReturn(expectedWallets);

        // Act
        List<wallets> result = walletController.getWalletsByUsername(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWallet, result.get(0));
        verify(walletRepository).getWalletByUsername("testuser");
    }

    @Test
    void changeBudget_ShouldCallRepository_WhenValidParameters() {
        // Arrange
        BigDecimal newBudget = new BigDecimal("2000.00");
        String username = "testuser";

        // Act
        walletController.changeBudget(newBudget, username);

        // Assert
        verify(walletRepository).changeBudget(newBudget, username);
    }

    @Test
    void getWalletsByUsername_ShouldReturnEmptyList_WhenNoWallets() {
        // Arrange
        when(authentication.getName()).thenReturn("userwithnowallet");
        when(walletRepository.getWalletByUsername("userwithnowallet")).thenReturn(Arrays.asList());

        // Act
        List<wallets> result = walletController.getWalletsByUsername(authentication);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(walletRepository).getWalletByUsername("userwithnowallet");
    }
}
