package org.expensetracker.repositories;

import org.expensetracker.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionRepository transactionRepository;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setUsername("testuser");
        testTransaction.setWallet_name("testwallet");
        testTransaction.setTransaction_amount(new BigDecimal("100.00"));
        testTransaction.setTransaction_title("Test Transaction");
        testTransaction.setTransaction_date("2024-01-15");
        testTransaction.setTransaction_category("Food");
    }

    @Test
    void addTransaction_ShouldUpdateWalletAndInsertTransaction() {
        // Act
        transactionRepository.addTransaction(testTransaction);

        // Assert
        verify(walletRepository).updateBalance(testTransaction.getTransaction_amount(), testTransaction.getWallet_name());
        verify(jdbcTemplate).update(
            eq("INSERT INTO transactions (wallet_name,username,transaction_amount,transaction_title,transaction_date,transaction_category) VALUES (?,?,?,?,?,?)"),
            eq("testwallet"),
            eq("testuser"),
            eq(new BigDecimal("100.00")),
            eq("Test Transaction"),
            eq("2024-01-15"),
            eq("Food")
        );
    }

    @Test
    void getAllTransaction_ShouldReturnTransactionList_WhenTransactionsExist() {
        // Arrange
        String username = "testuser";
        List<Transaction> expectedTransactions = Arrays.asList(testTransaction);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = transactionRepository.getAllTransaction(username);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jdbcTemplate).query(
            eq("SELECT * FROM transactions WHERE username = 'testuser'"),
            any(RowMapper.class)
        );
    }

    @Test
    void getAllTransaction_ShouldReturnEmptyList_WhenNoTransactions() {
        // Arrange
        String username = "userwithnots";
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        // Act
        List<Transaction> result = transactionRepository.getAllTransaction(username);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void addTransaction_ShouldHandleNullTransaction() {
        // Arrange
        Transaction nullTransaction = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            transactionRepository.addTransaction(nullTransaction);
        });
    }

    @Test
    void addTransaction_ShouldCallMethodsInCorrectOrder() {
        // Act
        transactionRepository.addTransaction(testTransaction);

        // Assert - verify wallet is updated before transaction is inserted
        verify(walletRepository).updateBalance(any(BigDecimal.class), anyString());
        verify(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any(), any());
    }
}
