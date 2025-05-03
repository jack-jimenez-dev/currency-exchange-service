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
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT unique_currency_pair UNIQUE (source_currency_code, target_currency_code)
);
