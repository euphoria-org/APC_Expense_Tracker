package org.expensetracker.controller;

import org.expensetracker.model.Transaction;
import org.expensetracker.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    @WithMockUser(username = "testuser")
    void testAddTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransaction_title("Test Transaction");
        transaction.setTransaction_amount(new BigDecimal("50.00"));
        transaction.setTransaction_category("Food");
        transaction.setTransaction_date("2025-09-09");

        mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"transaction_title\": \"Test Transaction\"," +
                        "\"transaction_amount\": 50.00," +
                        "\"transaction_category\": \"Food\"," +
                        "\"transaction_date\": \"2025-09-09\"}")
                .header("wallet_name_header", "testwallet")
                .with(csrf()))
                .andExpect(status().isOk());
        verify(transactionRepository, times(1)).addTransaction(any(Transaction.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllTransactions() throws Exception {
        Transaction t1 = new Transaction();
        t1.setUsername("testuser");
        t1.setTransaction_title("T1");
        t1.setTransaction_amount(new BigDecimal("10.00"));
        Transaction t2 = new Transaction();
        t2.setUsername("testuser");
        t2.setTransaction_title("T2");
        t2.setTransaction_amount(new BigDecimal("20.00"));
        List<Transaction> transactions = Arrays.asList(t1, t2);
        when(transactionRepository.getAllTransaction("testuser")).thenReturn(transactions);

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].transaction_title").value("T1"))
                .andExpect(jsonPath("$[1].transaction_title").value("T2"));
        verify(transactionRepository, times(1)).getAllTransaction("testuser");
    }
}

