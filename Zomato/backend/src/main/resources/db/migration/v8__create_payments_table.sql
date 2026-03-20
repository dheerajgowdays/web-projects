CREATE TABLE payments (
    id      BIGSERIAL PRIMARY KEY,
    order_id    BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    amount_paise  INTEGER NOT NULL CHECK (amount_paise > 0 ),
    payment_method  payment_method NOT NULL,
    status  payment_status NOT NULL DEFAULT 'PENDING',
    gateway_order_id    VARCHAR(100),
    gateway_payment_id  VARCHAR(100),
    
)