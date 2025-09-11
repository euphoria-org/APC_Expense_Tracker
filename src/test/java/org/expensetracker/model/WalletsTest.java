package org.expensetracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletsTest {

    private wallets wallet;

    @BeforeEach
    void setUp() {
        wallet = new wallets();
    }

    @Test
    void setAndGetWalletName_ShouldWorkCorrectly() {
        // Arrange
        String walletName = "My Wallet";

        // Act
        wallet.setWallet_name(walletName);

        // Assert
        assertEquals(walletName, wallet.getWallet_name());
    }

    @Test
    void setAndGetUsername_ShouldWorkCorrectly() {
        // Arrange
        String username = "testuser";

        // Act
        wallet.setUsername(username);

        // Assert
        assertEquals(username, wallet.getUsername());
    }

    @Test
    void setAndGetBalance_ShouldWorkCorrectly() {
        // Arrange
        BigDecimal balance = new BigDecimal("1000.50");

        // Act
        wallet.setBalanace(balance);

        // Assert
        assertEquals(balance, wallet.getBalanace());
    }

    @Test
    void setAndGetBudget_ShouldWorkCorrectly() {
        // Arrange
        BigDecimal budget = new BigDecimal("1500.00");

        // Act
        wallet.setBudget(budget);

        // Assert
        assertEquals(budget, wallet.getBudget());
    }

    @Test
    void wallet_ShouldHandleNullValues() {
        // Act & Assert
        assertNull(wallet.getWallet_name());
        assertNull(wallet.getUsername());
        assertNull(wallet.getBalanace());
        assertNull(wallet.getBudget());
    }

    @Test
    void wallet_ShouldAllowZeroBalance() {
        // Arrange
        BigDecimal zeroBalance = BigDecimal.ZERO;

        // Act
        wallet.setBalanace(zeroBalance);

        // Assert
        assertEquals(zeroBalance, wallet.getBalanace());
    }

    @Test
    void wallet_ShouldAllowNegativeBalance() {
        // Arrange
        BigDecimal negativeBalance = new BigDecimal("-100.00");

        // Act
        wallet.setBalanace(negativeBalance);

        // Assert
        assertEquals(negativeBalance, wallet.getBalanace());
    }

    @Test
    void wallet_ShouldHandleLargeNumbers() {
        // Arrange
        BigDecimal largeAmount = new BigDecimal("999999999.99");

        // Act
        wallet.setBudget(largeAmount);
        wallet.setBalanace(largeAmount);

        // Assert
        assertEquals(largeAmount, wallet.getBudget());
        assertEquals(largeAmount, wallet.getBalanace());
    }
}
