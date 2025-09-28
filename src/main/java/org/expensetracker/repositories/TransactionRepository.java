package org.expensetracker.repositories;

import org.expensetracker.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final WalletRepository walletRepository;
    public TransactionRepository(JdbcTemplate jdbcTemplate,WalletRepository walletRepository) {
        this.jdbcTemplate=jdbcTemplate;
        this.walletRepository = walletRepository;
    }

    public void addTransaction(Transaction transaction) {

        walletRepository.updateBalance(transaction.getTransaction_amount(),transaction.getWallet_name());
        String sql = "INSERT INTO transactions (wallet_name,username,transaction_amount,transaction_title,transaction_date,transaction_category) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,transaction.getWallet_name(),transaction.getUsername(),transaction.getTransaction_amount(),transaction.getTransaction_title(),transaction.getTransaction_date(),transaction.getTransaction_category());

    }

    public List<Transaction> getAllTransaction(String username) {
        String sql = "SELECT * FROM transactions WHERE username = '" + username + "'";
        RowMapper<Transaction> transactionRowMapper = (r, i) -> {
            Transaction rowObject = new Transaction();
            rowObject.setTransaction_amount(r.getBigDecimal("transaction_amount"));
            rowObject.setTransaction_title(r.getString("transaction_title"));
            rowObject.setTransaction_category(r.getString("transaction_category"));
            rowObject.setTransaction_date(r.getString("transaction_date"));
            rowObject.setWallet_name(r.getString("wallet_name"));
            rowObject.setUsername(r.getString("username"));
            return rowObject;
        };
        return jdbcTemplate.query(sql,transactionRowMapper);
    }

    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        RowMapper<Transaction> transactionRowMapper = (r, i) -> {
            Transaction rowObject = new Transaction();
            rowObject.setTransaction_id(r.getInt("transaction_id"));
            rowObject.setTransaction_amount(r.getBigDecimal("transaction_amount"));
            rowObject.setTransaction_title(r.getString("transaction_title"));
            rowObject.setTransaction_category(r.getString("transaction_category"));
            rowObject.setTransaction_date(r.getString("transaction_date"));
            rowObject.setWallet_name(r.getString("wallet_name"));
            rowObject.setUsername(r.getString("username"));
            return rowObject;
        };
        return jdbcTemplate.query(sql, transactionRowMapper);
    }
}
