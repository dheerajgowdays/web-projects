CREATE TABLE refresh_tokens (

    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT not NULL REFERENCES users(id) ON DELETE CASCADE,

    token_hash     VARCHAR(64) NOT NULL UNIQUE,
    device_info     VARCHAR(255),
    ip_address      INET,

    expires_at      TIMESTAMPTZ NOT NULL,
    is_revoked      BOOLEAN NOT NULL DEFAULT FALSE,
    revoked_at      TIMESTAMPTZ,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(tokEn_hash)
    WHERE is_revoked = FALSE;

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id)
    WHERE is_revoked = FALSE;

CREATE INDEX idx_refresh_tokens_expiry ON refresh_tokens(expires_at)
    WHERE is_revoked = FALSE;

CREATE TABLE otp_verifications (

    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users(id) ON DELETE CASCADE,
    identifier   VARCHAR(255) NOT NULL ,
    purpose     VARCHAR(50) NOT NULL,
    otp_hash    VARCHAR(64) NOT NULL,

    expires_at  TIMESTAMPTZ NOT NULL,

    attempt_count     SMALLINT NOT NULL DEFAULT 0,
    max_attempts      SMALLINT NOT NULL DEFAULT 5,
    
    is_used         BOOLEAN NOT NULL DEFAULT FALSE,
    used_at         TIMESTAMPTZ,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()

);

CREATE INDEX idx_otp_identifier_purpose
    ON otp_verifications(identifier , purpose)
    WHERE is_used = FALSE;

COMMENT ON COLUMN otp_verifications.otp_hash IS 'SHA-256 hash of the OTP. Never store plaintext OTPs';
COMMENT ON COLUMN refresh_tokens.token_hash IS 'SHA-256 hash of token. Never store plaintext tokens';