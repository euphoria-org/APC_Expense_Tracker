package org.expensetracker.controller;

import org.expensetracker.model.Transaction;
import org.expensetracker.model.users;
import org.expensetracker.repositories.TransactionRepository;
import org.expensetracker.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AdminController(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/users")
    public List<users> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
