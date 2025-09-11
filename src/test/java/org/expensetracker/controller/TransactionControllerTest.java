package org.expensetracker.controller;

import org.expensetracker.model.Transaction;
import org.expensetracker.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        
        testTransaction = new Transaction();
        testTransaction.setUsername("testuser");
        testTransaction.setWallet_name("testwallet");
        testTransaction.setTransaction_amount(new BigDecimal("100.00"));
        testTransaction.setTransaction_title("Test Transaction");
        testTransaction.setTransaction_date("2024-01-15");
        testTransaction.setTransaction_category("Food");
    }

    @Test
    void addTransaction_ShouldCallRepository_WhenValidTransaction() {
        // Arrange
        when(authentication.getName()).thenReturn("testuser");

        // Act
        transactionController.addTransaction(testTransaction, "testwallet", authentication);

        // Assert
        verify(transactionRepository).addTransaction(any(Transaction.class));
        assertEquals("testuser", testTransaction.getUsername());
        assertEquals("testwallet", testTransaction.getWallet_name());
    }

    @Test
    void getAllTransactions_ShouldReturnTransactionList_WhenCalled() {
        // Arrange
        List<Transaction> expectedTransactions = Arrays.asList(testTransaction);
        when(authentication.getName()).thenReturn("testuser");
        when(transactionRepository.getAllTransaction("testuser")).thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = transactionController.getAllTransactions(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTransaction, result.get(0));
        verify(transactionRepository).getAllTransaction("testuser");
    }

    @Test
    void addTransaction_ShouldSetCorrectUserAndWallet() {
        // Arrange
        when(authentication.getName()).thenReturn("authenticateduser");
        Transaction transaction = new Transaction();
        transaction.setTransaction_amount(new BigDecimal("50.00"));
        transaction.setTransaction_title("Test");

        // Act
        transactionController.addTransaction(transaction, "walletFromHeader", authentication);

        // Assert
        assertEquals("authenticateduser", transaction.getUsername());
        assertEquals("walletFromHeader", transaction.getWallet_name());
        verify(transactionRepository).addTransaction(transaction);
    }
}
