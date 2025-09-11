package org.expensetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class wallets {

    @Id
    private String wallet_name;
    private String username;
    private BigDecimal balance;

    private BigDecimal budget;

    public String getWallet_name() {
        return wallet_name;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalanace() {
        return balance;
    }

    public void setBalanace(BigDecimal balanace) {
        this.balance = balanace;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
