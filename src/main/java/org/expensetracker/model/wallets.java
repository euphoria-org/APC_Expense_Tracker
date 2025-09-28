package org.expensetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
public class wallets {

    @Id
    @NotBlank(message = "Wallet name is required")
    @Size(min = 1, max = 50, message = "Wallet name must be between 1 and 50 characters")
    private String wallet_name;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid balance format")
    private BigDecimal balance;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.01", message = "Budget must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid budget format")
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
