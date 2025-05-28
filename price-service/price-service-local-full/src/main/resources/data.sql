DROP TABLE IF EXISTS prices;

CREATE TABLE prices (
    brand_id BIGINT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    price_list INTEGER,
    product_id BIGINT,
    priority INTEGER,
    price DECIMAL(10,2),
    currency VARCHAR(3)
);

INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, currency) VALUES
(1, '2020-06-14 00:00:00', '2020-06-14 11:00:00', 1, 35455, 0, 35.50, 'EUR'),
(1, '2020-06-14 10:00:00', '2020-06-14 16:00:00', 2, 35455, 1, 50.00, 'EUR'),
(1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 3, 35455, 1, 25.45, 'EUR'),
(1, '2020-06-14 16:00:00', '2020-06-14 20:00:00', 4, 35455, 2, 44.00, 'EUR'),
(1, '2020-06-14 20:00:00', '2020-06-14 23:59:59', 5, 35455, 1, 39.99, 'EUR'),
(1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 6, 35455, 1, 30.50, 'EUR'),
(1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 7, 35455, 1, 38.95, 'EUR'),
(1, '2020-06-16 00:00:00', '2020-06-17 00:00:00', 8, 35455, 1, 41.00, 'EUR');
