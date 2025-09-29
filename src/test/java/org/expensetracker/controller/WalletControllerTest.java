package org.expensetracker.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.expensetracker.model.wallets;
import org.expensetracker.repositories.WalletRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

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
        var response = walletController.getWalletsByUsername(authentication);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testWallet, response.getBody().get(0));
        verify(walletRepository).getWalletByUsername("testuser");
    }

    @Test
    void changeBudget_ShouldCallRepository_WhenValidParameters() {
        // Arrange
        BigDecimal newBudget = new BigDecimal("2000.00");
        String username = "testuser";

        // Act
        when(authentication.getName()).thenReturn(username);

        var response = walletController.changeBudget(newBudget, username, authentication);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(Boolean.TRUE, body.get("success"));
        assertEquals(newBudget, body.get("newBudget"));
        verify(walletRepository).changeBudget(newBudget, username);
    }

    @Test
    void getWalletsByUsername_ShouldReturnEmptyList_WhenNoWallets() {
        // Arrange
        when(authentication.getName()).thenReturn("userwithnowallet");
        when(walletRepository.getWalletByUsername("userwithnowallet")).thenReturn(Arrays.asList());

        // Act
        var response = walletController.getWalletsByUsername(authentication);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(walletRepository).getWalletByUsername("userwithnowallet");
    }
}
