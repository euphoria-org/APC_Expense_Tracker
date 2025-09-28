// Admin Dashboard JavaScript

let currentView = 'users';

// Initialize the page
document.addEventListener('DOMContentLoaded', function() {
    showUsers();
});

// Show Users Table
function showUsers() {
    currentView = 'users';
    updateSidebarActive('users');
    updatePageHeader('Users Management', 'Manage all registered users in the system');
    
    showLoading();
    hideAllTables();
    
    // Fetch users data
    fetch('/api/admin/users')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch users');
            }
            return response.json();
        })
        .then(users => {
            populateUsersTable(users);
            showTable('usersTable');
            hideLoading();
        })
        .catch(error => {
            console.error('Error fetching users:', error);
            showError();
            hideLoading();
        });
}

// Show Transactions Table
function showTransactions() {
    currentView = 'transactions';
    updateSidebarActive('transactions');
    updatePageHeader('Transactions Management', 'View all transactions in the system');
    
    showLoading();
    hideAllTables();
    
    // Fetch transactions data
    fetch('/api/admin/transactions')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch transactions');
            }
            return response.json();
        })
        .then(transactions => {
            populateTransactionsTable(transactions);
            showTable('transactionsTable');
            hideLoading();
        })
        .catch(error => {
            console.error('Error fetching transactions:', error);
            showError();
            hideLoading();
        });
}

// Update sidebar active state
function updateSidebarActive(activeItem) {
    const sidebarItems = document.querySelectorAll('.sidebar-item');
    sidebarItems.forEach(item => {
        item.classList.remove('active');
    });
    
    if (activeItem === 'users') {
        sidebarItems[0].classList.add('active');
    } else if (activeItem === 'transactions') {
        sidebarItems[1].classList.add('active');
    }
}

// Update page header
function updatePageHeader(title, description) {
    document.getElementById('pageTitle').textContent = title;
    document.getElementById('pageDescription').textContent = description;
}

// Show loading state
function showLoading() {
    document.getElementById('loadingState').style.display = 'block';
    document.getElementById('errorState').style.display = 'none';
}

// Hide loading state
function hideLoading() {
    document.getElementById('loadingState').style.display = 'none';
}

// Show error state
function showError() {
    document.getElementById('errorState').style.display = 'block';
}

// Hide all tables
function hideAllTables() {
    document.getElementById('usersTable').style.display = 'none';
    document.getElementById('transactionsTable').style.display = 'none';
    document.getElementById('errorState').style.display = 'none';
}

// Show specific table
function showTable(tableId) {
    document.getElementById(tableId).style.display = 'block';
}

// Populate Users Table
function populateUsersTable(users) {
    const tbody = document.getElementById('usersTableBody');
    tbody.innerHTML = '';
    
    if (users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="p-4 text-center text-gray-400">No users found</td></tr>';
        return;
    }
    
    users.forEach(user => {
        const row = document.createElement('tr');
        row.className = 'table-row border-b border-gray-700';
        row.innerHTML = `
            <td class="p-4">${escapeHtml(user.username)}</td>
            <td class="p-4">
                <span class="px-2 py-1 rounded text-xs ${user.role === 'ADMIN' ? 'bg-red-600 text-white' : 'bg-blue-600 text-white'}">
                    ${escapeHtml(user.role)}
                </span>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Populate Transactions Table
function populateTransactionsTable(transactions) {
    const tbody = document.getElementById('transactionsTableBody');
    tbody.innerHTML = '';
    
    if (transactions.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="p-4 text-center text-gray-400">No transactions found</td></tr>';
        return;
    }
    
    transactions.forEach(transaction => {
        const row = document.createElement('tr');
        row.className = 'table-row border-b border-gray-700';
        row.innerHTML = `
            <td class="p-4">${transaction.transaction_id || 'N/A'}</td>
            <td class="p-4">${escapeHtml(transaction.username)}</td>
            <td class="p-4">${escapeHtml(transaction.wallet_name)}</td>
            <td class="p-4 ${transaction.transaction_amount < 0 ? 'text-red-400' : 'text-green-400'}">
                Rs. ${Math.abs(transaction.transaction_amount).toFixed(2)}
            </td>
            <td class="p-4">${escapeHtml(transaction.transaction_title)}</td>
            <td class="p-4">${escapeHtml(transaction.transaction_date)}</td>
            <td class="p-4">
                <span class="px-2 py-1 rounded text-xs bg-gray-600 text-white">
                    ${escapeHtml(transaction.transaction_category)}
                </span>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// View user details (placeholder function)
function viewUserDetails(username) {
    alert(`Viewing details for user: ${username}`);
    // In a real application, this would open a modal or navigate to a detail page
}

// Utility function to escape HTML
function escapeHtml(text) {
    if (text === null || text === undefined) return '';
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.toString().replace(/[&<>"']/g, function(m) { return map[m]; });
}
