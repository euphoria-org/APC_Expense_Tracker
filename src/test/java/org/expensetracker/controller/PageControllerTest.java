package org.expensetracker.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

    @InjectMocks
    private PageController pageController;

    @Test
    void login_ShouldReturnLoginPage() {
        // Act
        String result = pageController.login();

        // Assert
        assertEquals("login.html", result);
    }

    @Test
    void login_ShouldNotReturnNull() {
        // Act
        String result = pageController.login();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void login_ShouldReturnCorrectTemplateName() {
        // Act
        String result = pageController.login();

        // Assert
        assertTrue(result.endsWith(".html"));
        assertEquals("login.html", result);
    }
}
