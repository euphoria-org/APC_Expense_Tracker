package org.expensetracker.controller;

import org.expensetracker.model.Transaction;
import org.expensetracker.repositories.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {

    private Logger logger = Logger.getLogger(TransactionController.class.getName());
    private final TransactionRepository transactionRepository;
    
    // Valid transaction categories
    private static final String[] VALID_CATEGORIES = {
        "Food", "Educational expenses", "Transportation", "Entertainment", 
        "Utilities", "Healthcare", "Shopping", "Travel", "Home improvement", 
        "Gifts & Donations", "Insurance", "Investments", "Debt Payments", 
        "Subscriptions", "Personal Care", "Electronics", "Others"
    };

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addTransaction(
            @Valid @RequestBody Transaction transaction, 
            @RequestHeader @NotBlank(message = "Wallet name is required") String wallet_name_header, 
            Authentication authentication,
            BindingResult bindingResult) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            response.put("success", false);
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                if (errorMessage.length() > 0) errorMessage.append("; ");
                errorMessage.append(error.getDefaultMessage());
            });
            response.put("error", errorMessage.length() > 0 ? errorMessage.toString() : "Validation error occurred");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // Additional business logic validation
            String validationError = validateTransactionBusinessLogic(transaction);
            if (validationError != null) {
                response.put("success", false);
                response.put("error", validationError);
                return ResponseEntity.badRequest().body(response);
            }
            
            logger.info("Adding transaction for user: " + authentication.getName());
            transaction.setUsername(authentication.getName());
            transaction.setWallet_name(wallet_name_header);
            
            transactionRepository.addTransaction(transaction);
            
            response.put("success", true);
            response.put("message", "Transaction added successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.severe("Error adding transaction: " + e.getMessage());
            response.put("success", false);
            response.put("error", "Failed to add transaction. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(Authentication authentication) {
        try {
            List<Transaction> transactions = transactionRepository.getAllTransaction(authentication.getName());
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.severe("Error fetching transactions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private String validateTransactionBusinessLogic(Transaction transaction) {
        // Validate date format and range
        try {
            LocalDate transactionDate = LocalDate.parse(transaction.getTransaction_date(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();
            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate oneMonthFromNow = today.plusMonths(1);
            
            if (transactionDate.isBefore(oneYearAgo)) {
                return "Transaction date cannot be more than 1 year in the past";
            }
            if (transactionDate.isAfter(oneMonthFromNow)) {
                return "Transaction date cannot be more than 1 month in the future";
            }
        } catch (DateTimeParseException e) {
            return "Invalid date format. Please use YYYY-MM-DD format";
        }
        
        // Validate amount range
        BigDecimal amount = transaction.getTransaction_amount();
        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            return "Transaction amount cannot exceed 100,000";
        }
        
        // Validate category
        String category = transaction.getTransaction_category();
        boolean validCategory = false;
        for (String validCat : VALID_CATEGORIES) {
            if (validCat.equals(category)) {
                validCategory = true;
                break;
            }
        }
        if (!validCategory) {
            return "Invalid transaction category";
        }
        
        // Validate title length and content
        String title = transaction.getTransaction_title().trim();
        if (title.isEmpty()) {
            return "Transaction title cannot be empty";
        }
        if (title.length() > 100) {
            return "Transaction title cannot exceed 100 characters";
        }
        
        return null; // No validation errors
    }
}
