package org.expensetracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }

    @Test
    void setAndGetUsername_ShouldWorkCorrectly() {
        // Arrange
        String username = "testuser";

        // Act
        transaction.setUsername(username);

        // Assert
        assertEquals(username, transaction.getUsername());
    }

    @Test
    void setAndGetWalletName_ShouldWorkCorrectly() {
        // Arrange
        String walletName = "testwallet";

        // Act
        transaction.setWallet_name(walletName);

        // Assert
        assertEquals(walletName, transaction.getWallet_name());
    }

    @Test
    void setAndGetTransactionAmount_ShouldWorkCorrectly() {
        // Arrange
        BigDecimal amount = new BigDecimal("123.45");

        // Act
        transaction.setTransaction_amount(amount);

        // Assert
        assertEquals(amount, transaction.getTransaction_amount());
    }

    @Test
    void setAndGetTransactionTitle_ShouldWorkCorrectly() {
        // Arrange
        String title = "Grocery Shopping";

        // Act
        transaction.setTransaction_title(title);

        // Assert
        assertEquals(title, transaction.getTransaction_title());
    }

    @Test
    void setAndGetTransactionDate_ShouldWorkCorrectly() {
        // Arrange
        String date = "2024-01-15";

        // Act
        transaction.setTransaction_date(date);

        // Assert
        assertEquals(date, transaction.getTransaction_date());
    }

    @Test
    void setAndGetTransactionCategory_ShouldWorkCorrectly() {
        // Arrange
        String category = "Food";

        // Act
        transaction.setTransaction_category(category);

        // Assert
        assertEquals(category, transaction.getTransaction_category());
    }

    @Test
    void transaction_ShouldHandleNullValues() {
        // Act & Assert
        assertNull(transaction.getUsername());
        assertNull(transaction.getWallet_name());
        assertNull(transaction.getTransaction_amount());
        assertNull(transaction.getTransaction_title());
        assertNull(transaction.getTransaction_date());
        assertNull(transaction.getTransaction_category());
    }

    @Test
    void transaction_ShouldAllowZeroAmount() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act
        transaction.setTransaction_amount(zeroAmount);

        // Assert
        assertEquals(zeroAmount, transaction.getTransaction_amount());
    }
}
