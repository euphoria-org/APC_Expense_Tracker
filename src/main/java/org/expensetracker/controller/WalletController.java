package org.expensetracker.controller;

import org.expensetracker.model.wallets;
import org.expensetracker.repositories.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@Validated
public class WalletController {

    private Logger logger = Logger.getLogger(WalletController.class.getName());
    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    
    @GetMapping("/getWallets")
    public ResponseEntity<List<wallets>> getWalletsByUsername(Authentication authentication) {
        try {
            List<wallets> wallets = walletRepository.getWalletByUsername(authentication.getName());
            return ResponseEntity.ok(wallets);
        } catch (Exception e) {
            logger.severe("Error fetching wallets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/changeBudget")
    public ResponseEntity<Map<String, Object>> changeBudget(
            @RequestHeader @NotNull(message = "Budget is required")
            @DecimalMin(value = "0.01", message = "Budget must be greater than 0")
            @DecimalMax(value = "1000000", message = "Budget cannot exceed 1,000,000")
            BigDecimal newBudget,
            
            @RequestHeader @NotBlank(message = "Username is required") 
            String username,
            
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Security check: ensure user can only change their own budget
            if (!authentication.getName().equals(username)) {
                response.put("success", false);
                response.put("error", "Unauthorized: You can only change your own budget");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Additional validation: budget should be reasonable
            if (newBudget.scale() > 2) {
                response.put("success", false);
                response.put("error", "Budget can have at most 2 decimal places");
                return ResponseEntity.badRequest().body(response);
            }
            
            walletRepository.changeBudget(newBudget, username);
            
            response.put("success", true);
            response.put("message", "Budget updated successfully");
            response.put("newBudget", newBudget);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.severe("Error changing budget: " + e.getMessage());
            response.put("success", false);
            response.put("error", "Failed to update budget. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
