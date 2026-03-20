CREATE TABLE payments (
    id      BIGSERIAL PRIMARY KEY,
    order_id    BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    amount_paise  INTEGER NOT NULL CHECK (amount_paise > 0 ),
    payment_method  payment_method NOT NULL,
    status  payment_status NOT NULL DEFAULT 'PENDING',

    gateway_order_id    VARCHAR(100),
    gateway_payment_id  VARCHAR(100),
    gateway_signature VARCHAR(255),
    gateway_response JSONB,

    refund_id   VARCHAR(100),
    refund_amount_paise  INTEGER DEFAULT 0 ,
    refund_at   TIMESTAMPTZ,
    refund_reason   TEXT,

    initiated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    paid_at     TIMESTAMPTZ,
    failed_at   TIMESTAMPTZ,

    created_at   TIMESTAMPTZ not null DEFAULT now(),
    updated_at TIMESTAMPTZ not null DEFAULT now()
);

CREATE INDEX idx_payments_gateway_order ON payments(gateway_order_id);

CREATE INDEX idx_payments_status ON payments(status);

CREATE INDEX idx_payments_paid_at ON payments(paid_at)
    WHERE status = 'SUCCESS';

CREATE TRIGGER trigger_payments_update_at
    BEFORE UPDATE ON payments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN payments.gateway_response IS 'Raw Razorpay/Stripe response stored as JSONB for debugging and reconciliation';
COMMENT ON COLUMN payments.gateway_signature IS 'Must be verified using HMAC-SHA256 before trusting payment as successful';

