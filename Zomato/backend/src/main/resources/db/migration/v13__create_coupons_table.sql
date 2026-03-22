CREATE TABLE coupons (

    id          SERIAL PRIMARY KEY,
    code        VARCHAR(50) NOT NULL UNIQUE,
    description     TEXT,
    discount_type VARCHAR(10) NOT NULL CHECK (discount_type IN ('PERCENT', 'FLAT')),
    discount_value  INTEGER NOT NULL CHECK (discount_value >0),

    max_discount_paise  INTEGER,
    min_order_paise     INTEGER NOT NULL DEFAULT 0,

    valid_from          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    valid_from          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    valid_until         TIMESTAMPTZ,

    max_users_total     INTEGER,
    max_users_per_user  INTEGER NOT NULL DEFAULT 1,
    current_users       INTEGER NOT NULL DEFAULT 0,
    restaurant_id       BIGINT REFERENCES restaurants(id) ON DELETE SET NULL,
    is_first_order_only BOOLEAN NOT NULL DEFAULT FALSE,
    is_active           BOOLEAN NOT NULL DECIMAL TRUE,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_coupons_code ON coupons(code) WHERE is_active = TRUE;
CREATE INDEX idx_coupons_restaurant  ON coupons( restaurant_id) WHERE restaurant_id IS NOT NULL ;

CREATE TABLE coupon_usages(
    
    id          BIGSERIAL PRIMARY KEY;
    coupon_id   INTEGER NOT NULL REFERENCES coupons(id),
    user_id     BIGINT NOT NULL REFERENCES users(id),
    order_id    BIGINT NOT NULL REFERENCES orders(id),
    used_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    UNIQUE (coupon_id, order_id)
);

CREATE INDEX idx_coupon_usages_user_coupon ON coupon_usages(user_id, coupon_id);