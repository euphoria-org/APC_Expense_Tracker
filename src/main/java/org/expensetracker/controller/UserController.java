package org.expensetracker.controller;

import org.expensetracker.repositories.WalletRepository;
import org.expensetracker.service.UserDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class UserController {

    private final WalletRepository walletRepository;
    private final UserDetailService userDetailService;
    public UserController(UserDetailService userDetailService, WalletRepository walletRepository) {
        this.userDetailService=userDetailService;
        this.walletRepository=walletRepository;
    }
    @PostMapping("/signup")
    public void addUser(@RequestHeader String username, @RequestHeader String password, @RequestHeader String wallet_name, @RequestHeader BigDecimal budget) {
        userDetailService.addUser(username,password);
        BigDecimal balance = budget;
        walletRepository.addWallet(wallet_name,username,balance,budget);
    }

//    @GetMapping("/signup")
//    public String signup() {
//        return "signup";
//    }
}
