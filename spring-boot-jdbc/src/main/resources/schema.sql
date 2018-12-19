CREATE TABLE customers (
	id SERIAL,
	first_name VARCHAR(255) UNIQUE,
	last_name VARCHAR(255),
	balance NUMBER(12, 2)
);
