-- DROP DATABASE IF EXISTS smart_billing_db;

CREATE DATABASE smart_billing_db
    WITH
    OWNER = developer
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


CREATE TABLE login_user (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    user_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    auth_provider VARCHAR(20) DEFAULT 'local',
    google_id VARCHAR(255) UNIQUE,
    role_user VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);


CREATE TABLE customer (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tax_id VARCHAR(50) UNIQUE NOT NULL,
    name_customer VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(20),
    risk_score DECIMAL(5, 2) DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE TABLE contract (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    title VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE,
    monthly_fee DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) 
        REFERENCES customer(id) ON DELETE CASCADE
);


CREATE TABLE invoice (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    contract_id INTEGER NOT NULL,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    penalty_amount DECIMAL(15, 2) DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contract FOREIGN KEY (contract_id) 
        REFERENCES contracts(id) ON DELETE CASCADE
);


CREATE TABLE payment (
     id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    invoice_id INTEGER NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount_paid DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50),
    reference_number VARCHAR(100),
    CONSTRAINT fk_invoice FOREIGN KEY (invoice_id) 
        REFERENCES invoices(id) ON DELETE CASCADE
);
