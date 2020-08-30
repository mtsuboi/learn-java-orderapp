CREATE TABLE IF NOT EXISTS items (
	item_id VARCHAR(5) NOT NULL,
	item_name VARCHAR(255),
	item_price INTEGER,
	PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS orders (
	order_id VARCHAR(6) NOT NULL,
	order_status CHAR(1),
	order_date DATE,
	ship_date DATE,
	customer_name VARCHAR(255),
	customer_zipcode VARCHAR(7),
	customer_address VARCHAR(255),
	customer_tel VARCHAR(20),
	order_amount INTEGER,PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS order_detail (
	order_id VARCHAR(6) NOT NULL,
	order_detail_no INTEGER NOT NULL,
	item_id VARCHAR(5),
	item_name VARCHAR(255),
	item_price INTEGER,
	order_quantity INTEGER,
	order_detail_amount INTEGER,
	PRIMARY KEY (order_id,order_detail_no)
);
