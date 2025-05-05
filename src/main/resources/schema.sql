CREATE TABLE IF NOT EXISTS currencies (
    id IDENTITY PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id IDENTITY PRIMARY KEY,
    source_currency_code VARCHAR(3) NOT NULL,
    target_currency_code VARCHAR(3) NOT NULL,
    rate DECIMAL(19, 6) NOT NULL,
    buy_rate DECIMAL(19, 6),
    sell_rate DECIMAL(19, 6),
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT unique_currency_pair UNIQUE (source_currency_code, target_currency_code)
);

CREATE TABLE IF NOT EXISTS conversion_history (
    id IDENTITY PRIMARY KEY,
    source_currency_code VARCHAR(3) NOT NULL,
    target_currency_code VARCHAR(3) NOT NULL,
    original_amount DECIMAL(19, 6) NOT NULL,
    converted_amount DECIMAL(19, 6) NOT NULL,
    exchange_rate DECIMAL(19, 6) NOT NULL,
    conversion_date TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_id VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS users (
    id IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL
);
