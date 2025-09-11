package org.expensetracker.controller;

import org.expensetracker.repositories.WalletRepository;
import org.expensetracker.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserDetailService userDetailService;

    @InjectMocks
    private UserController userController;

    @Test
    void addUser_ShouldCreateUserAndWallet_WhenValidData() {
        // Arrange
        String username = "newuser";
        String password = "password123";
        String walletName = "mywallet";
        BigDecimal budget = new BigDecimal("1000.00");

        // Act
        userController.addUser(username, password, walletName, budget);

        // Assert
        verify(userDetailService).addUser(username, password);
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }

    @Test
    void addUser_ShouldSetBalanceEqualToBudget() {
        // Arrange
        String username = "testuser";
        String password = "testpass";
        String walletName = "testwallet";
        BigDecimal budget = new BigDecimal("500.00");

        // Act
        userController.addUser(username, password, walletName, budget);

        // Assert
        // Verify that balance is set equal to budget (as seen in the controller logic)
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }

    @Test
    void addUser_ShouldHandleZeroBudget() {
        // Arrange
        String username = "zerouser";
        String password = "pass";
        String walletName = "zerowallet";
        BigDecimal budget = BigDecimal.ZERO;

        // Act
        userController.addUser(username, password, walletName, budget);

        // Assert
        verify(userDetailService).addUser(username, password);
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }
}
