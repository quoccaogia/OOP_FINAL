DROP DATABASE IF EXISTS cua_hang_hoa;
CREATE DATABASE cua_hang_hoa;
USE cua_hang_hoa; -- chon database hien hanh cho phien lam viec

CREATE TABLE Supplier (
	supplier_id VARCHAR(10),
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(100) NOT NULL,
    representative VARCHAR(100),
    PRIMARY KEY(supplier_id)
);

CREATE TABLE Flower (
	flower_id VARCHAR(10),
    name VARCHAR(100) NOT NULL,
    category VARCHAR(20), -- loai hoa gi
    unit_price DECIMAL(8, 0) NOT NULL, -- decimal (bao nhieu chu so, bao nhieu chu so sau phay
    stock_quantity INT NOT NULL,
    color VARCHAR(10),
    supplier_id VARCHAR(10) NOT NULL,
    PRIMARY KEY(flower_id),
    FOREIGN KEY(supplier_id) REFERENCES Supplier(supplier_id)
);

CREATE TABLE Customer (
	customer_id VARCHAR(10),
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    PRIMARY KEY(customer_id)
);

CREATE TABLE Employee (
	employee_id VARCHAR(10),
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    salary DECIMAL(8, 0) NOT NULL,
    PRIMARY KEY(employee_id)
);

CREATE TABLE orders (
	order_id VARCHAR(10),
    customer_id VARCHAR(10),
    employee_id VARCHAR(10),
    order_date DATETIME NOT NULL,
    delivery_date DATETIME,
    delivery_address VARCHAR(100),
    order_status VARCHAR(50) NOT NULL,
    PRIMARY KEY(order_id),
    FOREIGN KEY(customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Orderdetail(
	order_id VARCHAR(10),
    flower_id VARCHAR(10),
    quantity INT NOT NULL,
    unit_price_at_sale DECIMAL(8, 0) NOT NULL,
    PRIMARY KEY(order_id, flower_id),
    FOREIGN KEY(order_id) REFERENCES orders(order_id),
    FOREIGN KEY(flower_id) REFERENCES Flower(flower_id)
);

CREATE TABLE Payment(
	payment_id VARCHAR(10),
    order_id VARCHAR(10) NOT NULL,
    amount DECIMAL(8, 0) NOT NULL, -- so tien thanh toan
    payment_method VARCHAR(20) NOT NULL,
    payment_date DATETIME,
    payment_status VARCHAR(50) NOT NULL,
    PRIMARY KEY(payment_id),
    FOREIGN KEY(order_id) REFERENCES orders(order_id)
);

CREATE TABLE Careschedule(
	care_id VARCHAR(10),
    flower_id VARCHAR(10) NOT NULL,
    employee_id VARCHAR(10) NOT NULL,
    check_date DATETIME,
    freshness_level VARCHAR(50) NOT NULL,
    water_status VARCHAR(50) NOT NULL,
    action_taken VARCHAR(50) NOT NULL,
    note VARCHAR(255),
    PRIMARY KEY(care_id),
    FOREIGN KEY(flower_id) REFERENCES Flower(flower_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);