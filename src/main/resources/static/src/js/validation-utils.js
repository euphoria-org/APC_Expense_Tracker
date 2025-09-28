/**
 * Global Validation Utilities for Expense Tracker Application
 * Provides reusable validation functions and patterns
 */

// Common validation patterns
const VALIDATION_PATTERNS = {
    username: /^[a-zA-Z0-9_]{3,20}$/,
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
    amount: /^\d+(\.\d{1,2})?$/,
    date: /^\d{4}-\d{2}-\d{2}$/,
    alphanumeric: /^[a-zA-Z0-9\s\-_.,!?()]+$/
};

// Common validation messages
const VALIDATION_MESSAGES = {
    required: "This field is required",
    username: {
        invalid: "Username must be 3-20 characters, only letters, numbers, and underscores",
        taken: "Username is already taken"
    },
    email: {
        invalid: "Please enter a valid email address"
    },
    password: {
        weak: "Password must be at least 8 characters with uppercase, lowercase, number, and special character",
        mismatch: "Passwords do not match"
    },
    amount: {
        invalid: "Amount must be a valid number with up to 2 decimal places",
        min: "Amount must be greater than 0",
        max: "Amount exceeds maximum limit"
    },
    date: {
        invalid: "Please enter a valid date",
        past: "Date cannot be in the past",
        future: "Date cannot be too far in the future"
    }
};

// Validation utility class
class ValidationUtils {
    
    /**
     * Validate username
     * @param {string} username - The username to validate
     * @returns {Object} - Validation result with isValid and message
     */
    static validateUsername(username) {
        if (!username || username.trim() === '') {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        if (!VALIDATION_PATTERNS.username.test(username.trim())) {
            return { isValid: false, message: VALIDATION_MESSAGES.username.invalid };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate email address
     * @param {string} email - The email to validate
     * @returns {Object} - Validation result with isValid and message
     */
    static validateEmail(email) {
        if (!email || email.trim() === '') {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        if (!VALIDATION_PATTERNS.email.test(email.trim())) {
            return { isValid: false, message: VALIDATION_MESSAGES.email.invalid };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate password strength
     * @param {string} password - The password to validate
     * @returns {Object} - Validation result with isValid and message
     */
    static validatePassword(password) {
        if (!password) {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        if (!VALIDATION_PATTERNS.password.test(password)) {
            return { isValid: false, message: VALIDATION_MESSAGES.password.weak };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate password confirmation
     * @param {string} password - The original password
     * @param {string} confirmPassword - The confirmation password
     * @returns {Object} - Validation result with isValid and message
     */
    static validatePasswordConfirmation(password, confirmPassword) {
        if (!confirmPassword) {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        if (password !== confirmPassword) {
            return { isValid: false, message: VALIDATION_MESSAGES.password.mismatch };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate monetary amount
     * @param {string|number} amount - The amount to validate
     * @param {number} min - Minimum allowed amount (default: 0.01)
     * @param {number} max - Maximum allowed amount (default: 1000000)
     * @returns {Object} - Validation result with isValid and message
     */
    static validateAmount(amount, min = 0.01, max = 1000000) {
        if (!amount || amount.toString().trim() === '') {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        const amountStr = amount.toString().trim();
        if (!VALIDATION_PATTERNS.amount.test(amountStr)) {
            return { isValid: false, message: VALIDATION_MESSAGES.amount.invalid };
        }
        
        const numAmount = parseFloat(amountStr);
        if (numAmount < min) {
            return { isValid: false, message: `${VALIDATION_MESSAGES.amount.min} (minimum: ${min})` };
        }
        
        if (numAmount > max) {
            return { isValid: false, message: `${VALIDATION_MESSAGES.amount.max} (maximum: ${max.toLocaleString()})` };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate date
     * @param {string} date - The date to validate (YYYY-MM-DD format)
     * @param {Object} constraints - Date constraints (allowPast, allowFuture, maxPastDays, maxFutureDays)
     * @returns {Object} - Validation result with isValid and message
     */
    static validateDate(date, constraints = {}) {
        const { allowPast = true, allowFuture = true, maxPastDays = 365, maxFutureDays = 30 } = constraints;
        
        if (!date || date.trim() === '') {
            return { isValid: false, message: VALIDATION_MESSAGES.required };
        }
        
        if (!VALIDATION_PATTERNS.date.test(date)) {
            return { isValid: false, message: VALIDATION_MESSAGES.date.invalid };
        }
        
        try {
            const inputDate = new Date(date);
            const today = new Date();
            today.setHours(0, 0, 0, 0);
            
            const diffTime = inputDate - today;
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            
            if (diffDays < 0 && !allowPast) {
                return { isValid: false, message: VALIDATION_MESSAGES.date.past };
            }
            
            if (diffDays > 0 && !allowFuture) {
                return { isValid: false, message: VALIDATION_MESSAGES.date.future };
            }
            
            if (diffDays < -maxPastDays) {
                return { isValid: false, message: `Date cannot be more than ${maxPastDays} days in the past` };
            }
            
            if (diffDays > maxFutureDays) {
                return { isValid: false, message: `Date cannot be more than ${maxFutureDays} days in the future` };
            }
            
        } catch (error) {
            return { isValid: false, message: VALIDATION_MESSAGES.date.invalid };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate text field
     * @param {string} text - The text to validate
     * @param {Object} constraints - Text constraints (minLength, maxLength, pattern, required)
     * @returns {Object} - Validation result with isValid and message
     */
    static validateText(text, constraints = {}) {
        const { minLength = 0, maxLength = 255, pattern = null, required = true } = constraints;
        
        if (!text || text.trim() === '') {
            if (required) {
                return { isValid: false, message: VALIDATION_MESSAGES.required };
            }
            return { isValid: true, message: "" };
        }
        
        const trimmedText = text.trim();
        
        if (trimmedText.length < minLength) {
            return { isValid: false, message: `Must be at least ${minLength} characters long` };
        }
        
        if (trimmedText.length > maxLength) {
            return { isValid: false, message: `Cannot exceed ${maxLength} characters` };
        }
        
        if (pattern && !pattern.test(trimmedText)) {
            return { isValid: false, message: "Contains invalid characters" };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Validate select/dropdown field
     * @param {string} value - The selected value
     * @param {Array} validOptions - Array of valid options
     * @param {boolean} required - Whether selection is required
     * @returns {Object} - Validation result with isValid and message
     */
    static validateSelect(value, validOptions = [], required = true) {
        if (!value || value.trim() === '' || value === 'Category') {
            if (required) {
                return { isValid: false, message: "Please make a selection" };
            }
            return { isValid: true, message: "" };
        }
        
        if (validOptions.length > 0 && !validOptions.includes(value)) {
            return { isValid: false, message: "Please select a valid option" };
        }
        
        return { isValid: true, message: "" };
    }
    
    /**
     * Sanitize input text
     * @param {string} text - Text to sanitize
     * @returns {string} - Sanitized text
     */
    static sanitizeInput(text) {
        if (!text) return '';
        return text.toString().trim().replace(/[<>]/g, '');
    }
    
    /**
     * Format currency amount
     * @param {number} amount - Amount to format
     * @param {string} currency - Currency symbol (default: 'Rs.')
     * @returns {string} - Formatted currency string
     */
    static formatCurrency(amount, currency = 'Rs.') {
        if (isNaN(amount)) return `${currency} 0.00`;
        return `${currency} ${parseFloat(amount).toFixed(2)}`;
    }
    
    /**
     * Check if date is within valid range for transactions
     * @param {string} date - Date string in YYYY-MM-DD format
     * @returns {boolean} - Whether date is valid for transactions
     */
    static isValidTransactionDate(date) {
        const result = this.validateDate(date, {
            allowPast: true,
            allowFuture: true,
            maxPastDays: 365,
            maxFutureDays: 30
        });
        return result.isValid;
    }
}

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { ValidationUtils, VALIDATION_PATTERNS, VALIDATION_MESSAGES };
} else {
    // Browser environment
    window.ValidationUtils = ValidationUtils;
    window.VALIDATION_PATTERNS = VALIDATION_PATTERNS;
    window.VALIDATION_MESSAGES = VALIDATION_MESSAGES;
}
