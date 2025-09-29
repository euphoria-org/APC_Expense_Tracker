package org.expensetracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.expensetracker.model.Transaction;
import org.expensetracker.repositories.TransactionRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private BindingResult bindingResult;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setUsername("testuser");
        testTransaction.setWallet_name("testwallet");
        testTransaction.setTransaction_amount(new BigDecimal("100.00"));
        testTransaction.setTransaction_title("Test Transaction");
        testTransaction.setTransaction_date(LocalDate.now().minusDays(1).toString());
        testTransaction.setTransaction_category("Food");
    }

    @Test
    void addTransaction_ShouldCallRepository_WhenValidTransaction() {
        // Arrange
        when(authentication.getName()).thenReturn("testuser");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.addTransaction(testTransaction, "testwallet", authentication, bindingResult);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(Boolean.TRUE, response.getBody().get("success"));
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
        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions(authentication);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testTransaction, response.getBody().get(0));
        verify(transactionRepository).getAllTransaction("testuser");
    }

    @Test
    void addTransaction_ShouldSetCorrectUserAndWallet() {
        // Arrange
        when(authentication.getName()).thenReturn("authenticateduser");
        when(bindingResult.hasErrors()).thenReturn(false);
        Transaction transaction = new Transaction();
        transaction.setTransaction_amount(new BigDecimal("50.00"));
        transaction.setTransaction_title("Test");
        transaction.setTransaction_category("Food");
        transaction.setTransaction_date(LocalDate.now().toString());

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.addTransaction(transaction, "walletFromHeader", authentication, bindingResult);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("authenticateduser", transaction.getUsername());
        assertEquals("walletFromHeader", transaction.getWallet_name());
        verify(transactionRepository).addTransaction(transaction);
    }
}
