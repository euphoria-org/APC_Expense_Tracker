package org.expensetracker.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {
    @Test
    void testTransactionGettersAndSetters() {
        Transaction transaction = new Transaction();
        transaction.setUsername("user1");
        transaction.setWallet_name("wallet1");
        transaction.setTransaction_amount(new BigDecimal("123.45"));
        transaction.setTransaction_title("Lunch");
        transaction.setTransaction_date("2025-09-09");
        transaction.setTransaction_category("Food");

        assertThat(transaction.getUsername()).isEqualTo("user1");
        assertThat(transaction.getWallet_name()).isEqualTo("wallet1");
        assertThat(transaction.getTransaction_amount()).isEqualTo(new BigDecimal("123.45"));
        assertThat(transaction.getTransaction_title()).isEqualTo("Lunch");
        assertThat(transaction.getTransaction_date()).isEqualTo("2025-09-09");
        assertThat(transaction.getTransaction_category()).isEqualTo("Food");
    }
}

