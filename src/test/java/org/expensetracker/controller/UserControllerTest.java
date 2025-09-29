package org.expensetracker.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.expensetracker.repositories.WalletRepository;
import org.expensetracker.service.UserDetailService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

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
        String password = "StrongP@ss1";
        String walletName = "mywallet";
        BigDecimal budget = new BigDecimal("1000.00");
        String confirmPassword = "StrongP@ss1";

        // Act
        var response = userController.addUser(username, password, walletName, budget, confirmPassword);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(Boolean.TRUE, body.get("success"));
        verify(userDetailService).addUser(username, password);
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }

    @Test
    void addUser_ShouldSetBalanceEqualToBudget() {
        // Arrange
        String username = "testuser";
        String password = "ValidP@ss2";
        String walletName = "testwallet";
        BigDecimal budget = new BigDecimal("500.00");
        String confirmPassword = "ValidP@ss2";

        // Act
        var response = userController.addUser(username, password, walletName, budget, confirmPassword);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        // Verify that balance is set equal to budget (as seen in the controller logic)
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }

    @Test
    void addUser_ShouldHandleZeroBudget() {
        // Arrange
        String username = "zerouser";
        String password = "ZeroP@ss3";
        String walletName = "zerowallet";
        BigDecimal budget = BigDecimal.ZERO;
        String confirmPassword = "ZeroP@ss3";

        // Act
        var response = userController.addUser(username, password, walletName, budget, confirmPassword);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(userDetailService).addUser(username, password);
        verify(walletRepository).addWallet(walletName, username, budget, budget);
    }
}
