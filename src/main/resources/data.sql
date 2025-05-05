-- Insert currencies
MERGE INTO currencies (id, code, name) KEY(code) VALUES (1, 'USD', 'Dólar Estadounidense');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (2, 'EUR', 'Euro');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (3, 'GBP', 'Libra Esterlina');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (4, 'JPY', 'Yen Japonés');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (5, 'CAD', 'Dólar Canadiense');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (6, 'AUD', 'Dólar Australiano');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (7, 'CHF', 'Franco Suizo');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (8, 'CNY', 'Yuan Chino');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (9, 'PEN', 'Sol Peruano');
MERGE INTO currencies (id, code, name) KEY(code) VALUES (10, 'MXN', 'Peso Mexicano');

-- Insert exchange rates (as of a sample date)
MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, buy_rate, sell_rate, last_updated)
VALUES (1, 'USD', 'EUR', 0.93, 0.92, 0.94, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, buy_rate, sell_rate, last_updated)
VALUES (2, 'EUR', 'USD', 1.07, 1.06, 1.08, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (3, 'USD', 'GBP', 0.79, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (4, 'GBP', 'USD', 1.27, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (5, 'USD', 'JPY', 151.67, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (6, 'JPY', 'USD', 0.0066, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (7, 'USD', 'CAD', 1.38, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (8, 'CAD', 'USD', 0.73, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (9, 'USD', 'AUD', 1.52, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (10, 'AUD', 'USD', 0.66, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (11, 'USD', 'CHF', 0.91, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (12, 'CHF', 'USD', 1.10, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (13, 'USD', 'CNY', 7.24, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (14, 'CNY', 'USD', 0.14, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, buy_rate, sell_rate, last_updated)
VALUES (15, 'USD', 'PEN', 3.72, 3.70, 3.74, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (16, 'PEN', 'USD', 0.27, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (17, 'USD', 'MXN', 16.76, CURRENT_TIMESTAMP());

MERGE INTO exchange_rates (id, source_currency_code, target_currency_code, rate, last_updated)
VALUES (18, 'MXN', 'USD', 0.060, CURRENT_TIMESTAMP());
