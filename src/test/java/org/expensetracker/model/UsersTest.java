package org.expensetracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    private users user;

    @BeforeEach
    void setUp() {
        user = new users();
    }

    @Test
    void setAndGetUsername_ShouldWorkCorrectly() {
        // Arrange
        String username = "testuser";

        // Act
        user.setUsername(username);

        // Assert
        assertEquals(username, user.getUsername());
    }

    @Test
    void setAndGetPassword_ShouldWorkCorrectly() {
        // Arrange
        String password = "hashedpassword123";

        // Act
        user.setPassword(password);

        // Assert
        assertEquals(password, user.getPassword());
    }

    @Test
    void user_ShouldHandleNullValues() {
        // Act & Assert
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    void user_ShouldHandleEmptyStrings() {
        // Arrange
        String emptyString = "";

        // Act
        user.setUsername(emptyString);
        user.setPassword(emptyString);

        // Assert
        assertEquals(emptyString, user.getUsername());
        assertEquals(emptyString, user.getPassword());
    }

    @Test
    void user_ShouldHandleLongUsernames() {
        // Arrange
        String longUsername = "a".repeat(99); // Just under the 100 char limit from schema

        // Act
        user.setUsername(longUsername);

        // Assert
        assertEquals(longUsername, user.getUsername());
    }

    @Test
    void user_ShouldHandleLongPasswords() {
        // Arrange
        String longPassword = "b".repeat(149); // Just under the 150 char limit from schema

        // Act
        user.setPassword(longPassword);

        // Assert
        assertEquals(longPassword, user.getPassword());
    }

    @Test
    void user_ShouldHandleSpecialCharactersInUsername() {
        // Arrange
        String specialUsername = "user@domain.com";

        // Act
        user.setUsername(specialUsername);

        // Assert
        assertEquals(specialUsername, user.getUsername());
    }
}
