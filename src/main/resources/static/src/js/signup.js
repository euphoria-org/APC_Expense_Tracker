// Validation patterns
const VALIDATION_PATTERNS = {
    username: /^[a-zA-Z0-9_]{3,20}$/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
    budget: /^\d+(\.\d{1,2})?$/
};

// Validation messages
const VALIDATION_MESSAGES = {
    username: {
        required: "Username is required",
        invalid: "Username must be 3-20 characters, only letters, numbers, and underscores"
    },
    password: {
        required: "Password is required",
        invalid: "Password must be at least 8 characters with uppercase, lowercase, number, and special character"
    },
    confirmPassword: {
        required: "Please confirm your password",
        mismatch: "Passwords do not match"
    },
    budget: {
        required: "Budget is required",
        invalid: "Budget must be a valid positive number",
        minimum: "Budget must be at least 1"
    }
};

// Validation functions
function validateUsername(username) {
    if (!username.trim()) {
        return { isValid: false, message: VALIDATION_MESSAGES.username.required };
    }
    if (!VALIDATION_PATTERNS.username.test(username)) {
        return { isValid: false, message: VALIDATION_MESSAGES.username.invalid };
    }
    return { isValid: true, message: "" };
}

function validatePassword(password) {
    if (!password) {
        return { isValid: false, message: VALIDATION_MESSAGES.password.required };
    }
    if (!VALIDATION_PATTERNS.password.test(password)) {
        return { isValid: false, message: VALIDATION_MESSAGES.password.invalid };
    }
    return { isValid: true, message: "" };
}

function validateConfirmPassword(password, confirmPassword) {
    if (!confirmPassword) {
        return { isValid: false, message: VALIDATION_MESSAGES.confirmPassword.required };
    }
    if (password !== confirmPassword) {
        return { isValid: false, message: VALIDATION_MESSAGES.confirmPassword.mismatch };
    }
    return { isValid: true, message: "" };
}

function validateBudget(budget) {
    if (!budget.trim()) {
        return { isValid: false, message: VALIDATION_MESSAGES.budget.required };
    }
    if (!VALIDATION_PATTERNS.budget.test(budget)) {
        return { isValid: false, message: VALIDATION_MESSAGES.budget.invalid };
    }
    const budgetValue = parseFloat(budget);
    if (budgetValue < 1) {
        return { isValid: false, message: VALIDATION_MESSAGES.budget.minimum };
    }
    return { isValid: true, message: "" };
}

// Show validation error
function showValidationError(inputElement, message) {
    const parentElement = inputElement.closest('.wrap-input100');
    parentElement.classList.add('alert-validate');
    parentElement.setAttribute('data-validate', message);
}

// Hide validation error
function hideValidationError(inputElement) {
    const parentElement = inputElement.closest('.wrap-input100');
    parentElement.classList.remove('alert-validate');
}

// Show success notification
function showSuccessNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'notification success';
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fa-solid fa-circle-check"></i>
            <span>${message}</span>
        </div>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Show error notification
function showErrorNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'notification error';
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>${message}</span>
        </div>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 5000);
}

// Real-time validation setup
function setupRealTimeValidation() {
    const usernameInput = document.querySelector(".username-input");
    const passwordInput = document.querySelector(".password-input");
    const confirmPasswordInput = document.querySelector(".confirm-password-input");
    const budgetInput = document.querySelector(".budget-input");

    // Username validation
    usernameInput.addEventListener('blur', function() {
        const result = validateUsername(this.value);
        if (!result.isValid) {
            showValidationError(this, result.message);
        } else {
            hideValidationError(this);
        }
    });

    usernameInput.addEventListener('input', function() {
        if (this.value.trim()) {
            hideValidationError(this);
        }
    });

    // Password validation
    passwordInput.addEventListener('blur', function() {
        const result = validatePassword(this.value);
        if (!result.isValid) {
            showValidationError(this, result.message);
        } else {
            hideValidationError(this);
        }
        
        // Also validate confirm password if it has a value
        if (confirmPasswordInput.value) {
            const confirmResult = validateConfirmPassword(this.value, confirmPasswordInput.value);
            if (!confirmResult.isValid) {
                showValidationError(confirmPasswordInput, confirmResult.message);
            } else {
                hideValidationError(confirmPasswordInput);
            }
        }
    });

    passwordInput.addEventListener('input', function() {
        if (this.value) {
            hideValidationError(this);
        }
    });

    // Confirm password validation
    confirmPasswordInput.addEventListener('blur', function() {
        const result = validateConfirmPassword(passwordInput.value, this.value);
        if (!result.isValid) {
            showValidationError(this, result.message);
        } else {
            hideValidationError(this);
        }
    });

    confirmPasswordInput.addEventListener('input', function() {
        if (this.value) {
            hideValidationError(this);
        }
    });

    // Budget validation
    budgetInput.addEventListener('blur', function() {
        const result = validateBudget(this.value);
        if (!result.isValid) {
            showValidationError(this, result.message);
        } else {
            hideValidationError(this);
        }
    });

    budgetInput.addEventListener('input', function() {
        if (this.value.trim()) {
            hideValidationError(this);
        }
    });
}

// Form submission
const submitBtn = document.querySelector(".login100-form-btn");
const signupForm = document.getElementById("signupForm");

// Prevent default form submission
signupForm.addEventListener("submit", function(event) {
    event.preventDefault();
});

submitBtn.addEventListener("click", function(event) {
    event.preventDefault();
    
    const username = document.querySelector(".username-input").value;
    const password = document.querySelector(".password-input").value;
    const confirmPassword = document.querySelector(".confirm-password-input").value;
    const budget = document.querySelector(".budget-input").value;
    
    console.log('Form submission attempted with:', {
        username: username,
        password: password ? '***' : 'empty',
        confirmPassword: confirmPassword ? '***' : 'empty',
        budget: budget,
        passwordsMatch: password === confirmPassword
    });
    
    // Early check: passwords must match
    if (password !== confirmPassword) {
        showErrorNotification("Passwords do not match!");
        showValidationError(document.querySelector(".confirm-password-input"), "Passwords do not match");
        return;
    }
    
    // Validate all fields
    const usernameResult = validateUsername(username);
    const passwordResult = validatePassword(password);
    const confirmPasswordResult = validateConfirmPassword(password, confirmPassword);
    const budgetResult = validateBudget(budget);
    
    let hasErrors = false;
    
    // Show validation errors
    if (!usernameResult.isValid) {
        showValidationError(document.querySelector(".username-input"), usernameResult.message);
        hasErrors = true;
    }
    
    if (!passwordResult.isValid) {
        showValidationError(document.querySelector(".password-input"), passwordResult.message);
        hasErrors = true;
    }
    
    if (!confirmPasswordResult.isValid) {
        showValidationError(document.querySelector(".confirm-password-input"), confirmPasswordResult.message);
        hasErrors = true;
    }
    
    if (!budgetResult.isValid) {
        showValidationError(document.querySelector(".budget-input"), budgetResult.message);
        hasErrors = true;
    }
    
    if (hasErrors) {
        showErrorNotification("Please fix the validation errors before submitting.");
        return;
    }
    
    // Show loading state
    submitBtn.disabled = true;
    submitBtn.textContent = "Creating Account...";
    
    const url = "http://localhost:8080/signup";
    const wallet_name = username + "_wallet";

    const headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('username', username);
    headers.append('password', password);
    headers.append('confirmPassword', confirmPassword);
    headers.append('budget', budget);
    headers.append('wallet_name', wallet_name);

    fetch(url, {
        method: 'POST',
        headers: headers,
    })
    .then(response => {
        if (response.ok) {
            showSuccessNotification("Account created successfully! Redirecting to login...");
            setTimeout(() => {
                window.location.href = "/login";
            }, 2000);
        } else if (response.status === 409) {
            showErrorNotification("Username already exists. Please choose a different username.");
            showValidationError(document.querySelector(".username-input"), "Username already exists");
        } else {
            throw new Error(`Server error: ${response.status}`);
        }
    })
    .catch(error => {
        console.error('Error occurred:', error);
        showErrorNotification("An error occurred while creating your account. Please try again.");
    })
    .finally(() => {
        // Reset button state
        submitBtn.disabled = false;
        submitBtn.textContent = "Signup";
    });
});

// Initialize real-time validation when page loads
document.addEventListener('DOMContentLoaded', function() {
    setupRealTimeValidation();
});