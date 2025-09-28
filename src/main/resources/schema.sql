CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(100) PRIMARY KEY,
    password_hash VARCHAR(150) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS wallets (
    wallet_name VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) REFERENCES users(username),
    balance DOUBLE PRECISION DEFAULT 0,
    budget DOUBLE PRECISION DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id SERIAL PRIMARY KEY,
    wallet_name VARCHAR(50) REFERENCES wallets(wallet_name),
    username VARCHAR(100) REFERENCES users(username),
    transaction_amount DOUBLE PRECISION NOT NULL,
    transaction_title VARCHAR(100) NOT NULL,
    transaction_date VARCHAR(15) NOT NULL,
    transaction_category VARCHAR(50) NOT NULL
);

-- Insert default admin user
INSERT INTO users (username, password_hash, role) VALUES ('admin', '$2a$10$8aUu/DCwhRcHx8uwG.E4TeglD5Kes9zc3pIEf52Z6WE3Z9gWatr5K', 'ADMIN') ON CONFLICT (username) DO NOTHING;