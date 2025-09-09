package org.expensetracker.repositories;

import org.expensetracker.model.wallets;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.List;

public interface WalletRepository extends JpaRepository<wallets,String> {

    @Query("SELECT w FROM wallets w WHERE w.username = :username")
    public List<wallets> getWalletByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("INSERT INTO wallets wallet (wallet.wallet_name, wallet.username, wallet.balance, wallet.budget) VALUES (:walletName, :username, :balance, :budget)")
    void addWallet(@Param("walletName") String walletName, @Param("username") String username, @Param("balance") BigDecimal balance, @Param("budget") BigDecimal budget);


    @Transactional
    @Modifying
    @Query("UPDATE wallets SET balance = balance - :amount WHERE wallet_name = :wallet_name")
    public void updateBalance(@Param("amount") BigDecimal amount, @Param("wallet_name") String username);

    @Modifying
    @Transactional
    @Query("UPDATE wallets w SET w.balance = (:newBudget - w.budget + w.balance) , w.budget = :newBudget  WHERE w.username = :username")
    void changeBudget(@Param("newBudget") BigDecimal newBudget, @Param("username") String username);
}
