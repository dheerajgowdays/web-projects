CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name   VARCHAR(100) NOT NULL,
    email  VARCHAR(255) NOT NULL unique,
    password_hash VARCHAR(255),
    phone   VARCHAR(15) unique,
    porfile_picture_url TEXT,
    role user_role NUll DEFAULT 'CUSTOMER',
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_login_at TIMESTAMPTZ,
    CONSTRAINT chk_email_lowercase CHECK (email = LOWER(email)),
    CONSTRAINT chk_email_format CHECK (email ~* '^[^@]+@[^@]+\.[^@]+$'),
    CONSTRAINT chk_phone_format CHECK (phone ~ '^+?[0-9]{7,15}$')
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active) WHERE is_active = TRUE;

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ lANGUAGE plpgsql;

CREATE TRIGGER trigger_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE users ID 'Central identity table for all platform users regardless of role';
COMMENT ON COLUMN users.password_hash IS 'BCrypt hash. NULL for OAuth-only accounts';
COMMENT ON COLUMN users.is_active IS 'Soft delete flag . Never hard-delete users';