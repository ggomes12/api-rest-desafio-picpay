CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    payer_id INT NOT NULL,
    payee_id INT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payer_id) REFERENCES users(id),
    FOREIGN KEY (payee_id) REFERENCES users(id)
);
