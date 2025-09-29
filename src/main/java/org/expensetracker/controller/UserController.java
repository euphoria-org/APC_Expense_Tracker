package org.expensetracker.controller;

import org.expensetracker.repositories.WalletRepository;
import org.expensetracker.service.UserDetailService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.*;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class UserController {

    private final WalletRepository walletRepository;
    private final UserDetailService userDetailService;
    
    // Password validation pattern
    private static final java.util.regex.Pattern PASSWORD_PATTERN = java.util.regex.Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
    );
    
    public UserController(UserDetailService userDetailService, WalletRepository walletRepository) {
        this.userDetailService = userDetailService;
        this.walletRepository = walletRepository;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> addUser(
            @RequestHeader @NotBlank(message = "Username is required") 
            @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores") 
            String username,
            
            @RequestHeader @NotBlank(message = "Password is required") 
            String password,
            
            @RequestHeader @NotBlank(message = "Wallet name is required") 
            String wallet_name,
            
            @RequestHeader @NotNull(message = "Budget is required")
            @DecimalMin(value = "0.01", message = "Budget must be greater than 0")
            BigDecimal budget,
            
            @RequestHeader(required = false) String confirmPassword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate password complexity
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                response.put("success", false);
                response.put("error", "Password must be at least 8 characters with uppercase, lowercase, number, and one of the following special characters: @ $ ! % * # ? &");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate password confirmation
            if (confirmPassword == null || !password.equals(confirmPassword)) {
                response.put("success", false);
                response.put("error", "Passwords do not match");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate budget range (reasonable limits)
            if (budget.compareTo(new BigDecimal("1000000")) > 0) {
                response.put("success", false);
                response.put("error", "Budget cannot exceed 1,000,000");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create user and wallet
            userDetailService.addUser(username, password);
            BigDecimal balance = budget;
            walletRepository.addWallet(wallet_name, username, balance, budget);
            
            response.put("success", true);
            response.put("message", "User created successfully");
            return ResponseEntity.ok(response);
            
        } catch (DataIntegrityViolationException e) {
            response.put("success", false);
            response.put("error", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "An error occurred while creating the account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
