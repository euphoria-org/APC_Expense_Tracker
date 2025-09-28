const settingsBtn = document.querySelector(".settings");
settingsBtn.classList.remove("font-semibold");
settingsBtn.classList.add("sidebar-hover","text-gray-800","font-bold");

// Budget validation rules
const BUDGET_VALIDATION = {
    pattern: /^\d+(\.\d{1,2})?$/,
    min: 0.01,
    max: 1000000,
    required: true
};

// Budget validation messages
const BUDGET_MESSAGES = {
    required: "Budget is required",
    invalid: "Budget must be a valid number with up to 2 decimal places",
    min: "Budget must be at least 0.01",
    max: "Budget cannot exceed 1,000,000",
    unchanged: "Please enter a different budget amount"
};

async function getWallets() {
    try {
        const response = await fetch("http://localhost:8080/getWallets");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('Error fetching wallets:', error);
        showSettingsNotification("Failed to load wallet information. Please refresh the page.", "error");
        return null;
    }
}

// Validate budget input
function validateBudget(budget, currentBudget = null) {
    if (!budget || budget.trim() === '') {
        return { isValid: false, message: BUDGET_MESSAGES.required };
    }

    if (!BUDGET_VALIDATION.pattern.test(budget)) {
        return { isValid: false, message: BUDGET_MESSAGES.invalid };
    }

    const numBudget = parseFloat(budget);
    if (numBudget < BUDGET_VALIDATION.min) {
        return { isValid: false, message: BUDGET_MESSAGES.min };
    }

    if (numBudget > BUDGET_VALIDATION.max) {
        return { isValid: false, message: BUDGET_MESSAGES.max };
    }

    if (currentBudget && numBudget === parseFloat(currentBudget)) {
        return { isValid: false, message: BUDGET_MESSAGES.unchanged };
    }

    return { isValid: true, message: "" };
}

// Show settings validation error
function showSettingsError(inputElement, message) {
    // Remove existing error
    hideSettingsError(inputElement);
    
    // Create error element
    const errorElement = document.createElement('div');
    errorElement.className = 'settings-error';
    errorElement.textContent = message;
    
    // Add error styling to input
    inputElement.style.borderColor = '#ef4444';
    inputElement.style.backgroundColor = 'rgba(239, 68, 68, 0.1)';
    
    // Insert error element after input container
    const container = inputElement.closest('.m-b-c-input') || inputElement.parentNode;
    container.appendChild(errorElement);
}

// Hide settings validation error
function hideSettingsError(inputElement) {
    // Remove error styling
    inputElement.style.borderColor = '';
    inputElement.style.backgroundColor = '';
    
    // Remove error element
    const container = inputElement.closest('.m-b-c-input') || inputElement.parentNode;
    const errorElement = container.querySelector('.settings-error');
    if (errorElement) {
        errorElement.remove();
    }
}

// Show settings notification
function showSettingsNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `settings-notification ${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fa-solid fa-${type === 'success' ? 'circle-check' : 'circle-exclamation'}"></i>
            <span>${message}</span>
        </div>
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, type === 'success' ? 3000 : 5000);
}

// Setup real-time validation for budget input
function setupBudgetValidation() {
    const budgetInput = document.querySelector(".change-budget-input");
    
    if (budgetInput) {
        budgetInput.addEventListener('blur', function() {
            const result = validateBudget(this.value);
            if (!result.isValid) {
                showSettingsError(this, result.message);
            } else {
                hideSettingsError(this);
            }
        });
        
        budgetInput.addEventListener('input', function() {
            if (this.value.trim()) {
                hideSettingsError(this);
            }
        });
        
        // Set current budget as placeholder
        getWallets().then(data => {
            if (data && data.length > 0) {
                budgetInput.placeholder = `Current: ${data[0].budget}`;
                budgetInput.setAttribute('data-current-budget', data[0].budget);
            }
        });
    }
}

const changeBudgetBtn = document.querySelector(".m-b-c-heading-button");

changeBudgetBtn.addEventListener("click", async function(event) {
    event.preventDefault();
    
    const budgetInput = document.querySelector(".change-budget-input");
    const newBudget = budgetInput.value;
    const currentBudget = budgetInput.getAttribute('data-current-budget');
    
    // Validate budget
    const validationResult = validateBudget(newBudget, currentBudget);
    if (!validationResult.isValid) {
        showSettingsError(budgetInput, validationResult.message);
        showSettingsNotification(validationResult.message, "error");
        return;
    }
    
    // Show loading state
    const originalText = changeBudgetBtn.textContent;
    changeBudgetBtn.disabled = true;
    changeBudgetBtn.textContent = "Updating...";
    
    try {
        const walletData = await getWallets();
        if (!walletData || walletData.length === 0) {
            throw new Error("Unable to load wallet information");
        }
        
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('username', walletData[0].username);
        headers.append('newBudget', newBudget);
        
        const response = await fetch("http://localhost:8080/changeBudget", {
            method: 'POST',
            headers: headers,
        });
        
        const responseData = await response.json();
        
        if (response.ok && responseData.success) {
            showSettingsNotification("Budget updated successfully!", "success");
            budgetInput.value = "";
            budgetInput.placeholder = `Current: ${responseData.newBudget}`;
            budgetInput.setAttribute('data-current-budget', responseData.newBudget);
            hideSettingsError(budgetInput);
        } else {
            const errorMessage = responseData.error || "Failed to update budget";
            showSettingsError(budgetInput, errorMessage);
            showSettingsNotification(errorMessage, "error");
        }
    } catch (error) {
        console.error('Error updating budget:', error);
        const errorMessage = "Network error. Please check your connection and try again.";
        showSettingsError(budgetInput, errorMessage);
        showSettingsNotification(errorMessage, "error");
    } finally {
        // Reset button state
        changeBudgetBtn.disabled = false;
        changeBudgetBtn.textContent = originalText;
    }
});

// Initialize validation when page loads
document.addEventListener('DOMContentLoaded', function() {
    setupBudgetValidation();
});
