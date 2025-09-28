// sidebar adjustment
const dashboardBtn = document.querySelector(".Dashboard");
dashboardBtn.classList.remove("font-semibold");
dashboardBtn.classList.add("sidebar-hover","text-gray-800","font-bold");

// declaring consts
const budget80Toast = document.querySelector(".main-toast-budget-limit-cont");
const transSuccessToast = document.querySelector(".main-toast-trans-success-cont");
const transFailedToast = document.querySelector(".main-toast-trans-failed-cont");

const budgetContainer = document.querySelector(".row1-budget-amount");
const balanceContainer = document.querySelector(".row1-balance-amount");
const expenseContainer = document.querySelector(".row1-expense-amount");

// updating dashboard date 

const currentDate = new Date();
let curDay = currentDate.getDate();
let curMonth = currentDate.getMonth() + 1; // Months are zero-based
let curYear = currentDate.getFullYear();

if(curDay<10) {
  curDay = `0${curDay}`;
}
if(curMonth<10) {
  curMonth = `0${curMonth}`
}
const mainBodyDateContainer = document.querySelector(".main-body-date-text");
mainBodyDateContainer.innerHTML=`${curDay}-${curMonth}-${curYear}`;

// updating dashboard

async function getWallets() {
    return fetch("http://localhost:8080/getWallets")
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      // Do whatever you want with the JSON data
      console.log(data);
      return data; // Return the data fetched from the server
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error);
    });
}

const userGreet = document.querySelector(".user-greet");


getWallets().then(data => {
    let budget = data[0].budget;
    let balance = data[0].balanace;
    if(balance <= 0.2*budget) {
      console.log("if less than 80")
      budget80Toast.classList.remove("hidden");
    } 
    budgetContainer.innerHTML = `Rs.${budget}`;
    balanceContainer.innerHTML = `Rs.${balance}`;
    expenseContainer.innerHTML = `Rs.${budget - balance}`;
    userGreet.innerHTML = `Hello , ${data[0].username}`;
})


async function performTransaction(amount, title, date, category) {
    try {
        const wallets = await getWallets();
        const currentBalance = wallets[0].balanace;
        const transactionAmount = parseFloat(amount);
        
        if (currentBalance >= transactionAmount) {
            // Hide all toasts initially
            budget80Toast.classList.add("hidden");
            transFailedToast.classList.add("hidden");
            transSuccessToast.classList.add("hidden");

            const url = "http://localhost:8080/transaction";
            const transactionData = {
                "transaction_amount": transactionAmount,
                "transaction_title": title.trim(),
                "transaction_date": date,
                "transaction_category": category
            };

            // Disable submit button during transaction
            const submitBtn = document.querySelector(".m-b-r-top-form-submit");
            const originalText = submitBtn.textContent;
            submitBtn.disabled = true;
            submitBtn.textContent = "Processing...";

            try {
                const walletName = wallets[0].wallet_name;
                const headers = new Headers();
                headers.append('Content-Type', 'application/json');
                headers.append('wallet_name_header', walletName);

                const response = await fetch(url, {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(transactionData)
                });

                const responseData = await response.json();

                if (response.ok && responseData.success) {
                    // Success
                    transSuccessToast.classList.remove("hidden");
                    showTransactionNotification("Transaction added successfully!", "success");
                    
                    // Clear form
                    document.querySelector(".m-b-r-top-form-amount").value = "";
                    document.querySelector(".m-b-r-top-form-title").value = "";
                    document.querySelector(".m-b-r-top-form-date").value = "";
                    document.querySelector(".m-b-r-top-form-category").value = "Category";
                    
                    // Reload page after success message
                    setTimeout(() => {
                        location.reload();
                    }, 2000);
                } else {
                    // Server validation error
                    transFailedToast.classList.remove("hidden");
                    const errorMessage = responseData.error || "Transaction failed. Please try again.";
                    showTransactionNotification(errorMessage, "error");
                }
            } catch (fetchError) {
                console.error('Network error:', fetchError);
                transFailedToast.classList.remove("hidden");
                showTransactionNotification("Network error. Please check your connection and try again.", "error");
            } finally {
                // Re-enable submit button
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
            }
        } else {
            // Insufficient balance
            budget80Toast.classList.add("hidden");
            transSuccessToast.classList.add("hidden");
            transFailedToast.classList.remove("hidden");
            showTransactionNotification("Insufficient balance for this transaction.", "error");
        }
    } catch (error) {
        console.error('Error in performTransaction:', error);
        showTransactionNotification("An unexpected error occurred. Please try again.", "error");
    }
}

// Transaction validation patterns and rules
const TRANSACTION_VALIDATION = {
    amount: {
        pattern: /^\d+(\.\d{1,2})?$/,
        min: 0.01,
        max: 100000,
        required: true
    },
    title: {
        minLength: 1,
        maxLength: 100,
        required: true,
        pattern: /^[a-zA-Z0-9\s\-_.,!?()]+$/
    },
    date: {
        required: true,
        pattern: /^\d{4}-\d{2}-\d{2}$/
    },
    category: {
        required: true,
        validOptions: [
            "Food", "Educational expenses", "Transportation", "Entertainment",
            "Utilities", "Healthcare", "Shopping", "Travel", "Home improvement",
            "Gifts & Donations", "Insurance", "Investments", "Debt Payments",
            "Subscriptions", "Personal Care", "Electronics", "Others"
        ]
    }
};

// Transaction validation messages
const TRANSACTION_MESSAGES = {
    amount: {
        required: "Amount is required",
        invalid: "Amount must be a valid number with up to 2 decimal places",
        min: "Amount must be at least 0.01",
        max: "Amount cannot exceed 100,000"
    },
    title: {
        required: "Transaction title is required",
        minLength: "Title cannot be empty",
        maxLength: "Title cannot exceed 100 characters",
        invalid: "Title contains invalid characters"
    },
    date: {
        required: "Date is required",
        invalid: "Please select a valid date",
        pastLimit: "Date cannot be more than 1 year in the past",
        futureLimit: "Date cannot be more than 1 month in the future"
    },
    category: {
        required: "Please select a category",
        invalid: "Please select a valid category"
    }
};

// Validate transaction amount
function validateTransactionAmount(amount) {
    if (!amount || amount.trim() === '') {
        return { isValid: false, message: TRANSACTION_MESSAGES.amount.required };
    }
    
    if (!TRANSACTION_VALIDATION.amount.pattern.test(amount)) {
        return { isValid: false, message: TRANSACTION_MESSAGES.amount.invalid };
    }
    
    const numAmount = parseFloat(amount);
    if (numAmount < TRANSACTION_VALIDATION.amount.min) {
        return { isValid: false, message: TRANSACTION_MESSAGES.amount.min };
    }
    
    if (numAmount > TRANSACTION_VALIDATION.amount.max) {
        return { isValid: false, message: TRANSACTION_MESSAGES.amount.max };
    }
    
    return { isValid: true, message: "" };
}

// Validate transaction title  
function validateTransactionTitle(title) {
    if (!title || title.trim() === '') {
        return { isValid: false, message: TRANSACTION_MESSAGES.title.required };
    }
    
    const trimmedTitle = title.trim();
    if (trimmedTitle.length < TRANSACTION_VALIDATION.title.minLength) {
        return { isValid: false, message: TRANSACTION_MESSAGES.title.minLength };
    }
    
    if (trimmedTitle.length > TRANSACTION_VALIDATION.title.maxLength) {
        return { isValid: false, message: TRANSACTION_MESSAGES.title.maxLength };
    }
    
    if (!TRANSACTION_VALIDATION.title.pattern.test(trimmedTitle)) {
        return { isValid: false, message: TRANSACTION_MESSAGES.title.invalid };
    }
    
    return { isValid: true, message: "" };
}

// Validate transaction date
function validateTransactionDate(date) {
    if (!date || date.trim() === '') {
        return { isValid: false, message: TRANSACTION_MESSAGES.date.required };
    }
    
    if (!TRANSACTION_VALIDATION.date.pattern.test(date)) {
        return { isValid: false, message: TRANSACTION_MESSAGES.date.invalid };
    }
    
    const selectedDate = new Date(date);
    const today = new Date();
    const oneYearAgo = new Date();
    oneYearAgo.setFullYear(today.getFullYear() - 1);
    const oneMonthFromNow = new Date();
    oneMonthFromNow.setMonth(today.getMonth() + 1);
    
    if (selectedDate < oneYearAgo) {
        return { isValid: false, message: TRANSACTION_MESSAGES.date.pastLimit };
    }
    
    if (selectedDate > oneMonthFromNow) {
        return { isValid: false, message: TRANSACTION_MESSAGES.date.futureLimit };
    }
    
    return { isValid: true, message: "" };
}

// Validate transaction category
function validateTransactionCategory(category) {
    if (!category || category.trim() === '' || category === 'Category') {
        return { isValid: false, message: TRANSACTION_MESSAGES.category.required };
    }
    
    if (!TRANSACTION_VALIDATION.category.validOptions.includes(category)) {
        return { isValid: false, message: TRANSACTION_MESSAGES.category.invalid };
    }
    
    return { isValid: true, message: "" };
}

// Show transaction validation error
function showTransactionError(inputElement, message) {
    // Remove existing error
    hideTransactionError(inputElement);
    
    // Create error element
    const errorElement = document.createElement('div');
    errorElement.className = 'transaction-error';
    errorElement.textContent = message;
    
    // Add error styling to input
    inputElement.style.borderColor = '#ef4444';
    inputElement.style.backgroundColor = 'rgba(239, 68, 68, 0.1)';
    
    // Insert error element after input
    inputElement.parentNode.insertBefore(errorElement, inputElement.nextSibling);
}

// Hide transaction validation error
function hideTransactionError(inputElement) {
    // Remove error styling
    inputElement.style.borderColor = '';
    inputElement.style.backgroundColor = '';
    
    // Remove error element
    const errorElement = inputElement.parentNode.querySelector('.transaction-error');
    if (errorElement) {
        errorElement.remove();
    }
}

// Show transaction notification
function showTransactionNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `transaction-notification ${type}`;
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

// Setup real-time validation for transaction form
function setupTransactionValidation() {
    const amountInput = document.querySelector(".m-b-r-top-form-amount");
    const titleInput = document.querySelector(".m-b-r-top-form-title");
    const dateInput = document.querySelector(".m-b-r-top-form-date");
    const categorySelect = document.querySelector(".m-b-r-top-form-category");
    
    // Amount validation
    amountInput.addEventListener('blur', function() {
        const result = validateTransactionAmount(this.value);
        if (!result.isValid) {
            showTransactionError(this, result.message);
        } else {
            hideTransactionError(this);
        }
    });
    
    amountInput.addEventListener('input', function() {
        if (this.value.trim()) {
            hideTransactionError(this);
        }
    });
    
    // Title validation
    titleInput.addEventListener('blur', function() {
        const result = validateTransactionTitle(this.value);
        if (!result.isValid) {
            showTransactionError(this, result.message);
        } else {
            hideTransactionError(this);
        }
    });
    
    titleInput.addEventListener('input', function() {
        if (this.value.trim()) {
            hideTransactionError(this);
        }
    });
    
    // Date validation
    dateInput.addEventListener('blur', function() {
        const result = validateTransactionDate(this.value);
        if (!result.isValid) {
            showTransactionError(this, result.message);
        } else {
            hideTransactionError(this);
        }
    });
    
    dateInput.addEventListener('change', function() {
        if (this.value) {
            hideTransactionError(this);
        }
    });
    
    // Category validation
    categorySelect.addEventListener('change', function() {
        const result = validateTransactionCategory(this.value);
        if (!result.isValid) {
            showTransactionError(this, result.message);
        } else {
            hideTransactionError(this);
        }
    });
}

// Initialize transaction form validation when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    setupTransactionValidation();
});

async function addTransaction(event) {
    event.preventDefault();
    
    const amount = document.querySelector(".m-b-r-top-form-amount").value;
    const title = document.querySelector(".m-b-r-top-form-title").value;
    const date = document.querySelector(".m-b-r-top-form-date").value;
    const category = document.querySelector(".m-b-r-top-form-category").value;
    
    // Validate all fields
    const amountResult = validateTransactionAmount(amount);
    const titleResult = validateTransactionTitle(title);
    const dateResult = validateTransactionDate(date);
    const categoryResult = validateTransactionCategory(category);
    
    let hasErrors = false;
    
    // Show validation errors
    if (!amountResult.isValid) {
        showTransactionError(document.querySelector(".m-b-r-top-form-amount"), amountResult.message);
        hasErrors = true;
    }
    
    if (!titleResult.isValid) {
        showTransactionError(document.querySelector(".m-b-r-top-form-title"), titleResult.message);
        hasErrors = true;
    }
    
    if (!dateResult.isValid) {
        showTransactionError(document.querySelector(".m-b-r-top-form-date"), dateResult.message);
        hasErrors = true;
    }
    
    if (!categoryResult.isValid) {
        showTransactionError(document.querySelector(".m-b-r-top-form-category"), categoryResult.message);
        hasErrors = true;
    }
    
    if (hasErrors) {
        showTransactionNotification("Please fix the validation errors before submitting.", "error");
        return;
    }
    
    // Perform transaction if validation passes
    await performTransaction(amount, title, date, category);
}

const submitBtn = document.querySelector(".m-b-r-top-form-submit");

submitBtn.addEventListener("click", addTransaction);


// Call the function with appropriate arguments




// updating transaction-history small version !
const row_3_h_list = document.querySelector(".row3-h-list");
fetchData().then(data => {
  data.sort((a, b) => new Date(b.transaction_date) - new Date(a.transaction_date));
  const topThreeRecentTransactions = data.slice(0, 3);
  for(let i in topThreeRecentTransactions) {
    let row3_h_list_entry = document.createElement("div");
    row3_h_list_entry.classList.add("row3-h-list","flex-col","max-h-14","text-ellipsis" , "overflow-hidden");
    row3_h_list_entry.innerHTML=`<div class="row3-list-entry flex justify-between py-1">
    <div class="list-entry-left w-28">
        <div class="entry-name text-white font-semibold">${topThreeRecentTransactions[i].transaction_title}</div>
        <div class="entry-date text-slate-300">${topThreeRecentTransactions[i].transaction_date}</div>
    </div>
    <div class="list-entry-right w-20">
        <div class="entry-amount text-white font-semibold">Rs.${topThreeRecentTransactions[i].transaction_amount}</div>
        <div class="entry-category text-slate-300">${topThreeRecentTransactions[i].transaction_category}</div>
    </div>
</div>`;
row_3_h_list.appendChild(row3_h_list_entry); 
}
  // console.log(topThreeRecentTransactions);
})


// updating data on mouse clicks!

