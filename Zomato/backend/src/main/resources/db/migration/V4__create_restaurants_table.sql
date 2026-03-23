CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255),
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    landmark    VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state  VARCHAR(100) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    cover_image_url TEXT,
    logo_url TEXT,
    price_range SMALLINT CHECK (price_range BETWEEN 1 AND 3),
    min_order_paise INTEGER NOT NULL DEFAULT  0,
    delivery_time_min SMALLINT NOT NULL DEFAULT 30,
    delivery_time_max SMALLINT NOT NULL DEFAULT 45,
    delivery_fee_paise INTEGER NOT NULL DEFAULT 0,
    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    total_rating INTEGER NOT NULL DEFAULT 0,
    is_open BOOLEAN NOT NULL DEFAULT FALSE,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    fssai_license VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_delivery_time CHECK (delivery_time_min <= delivery_time_max), 
    CONSTRAINT chk_rating_range CHECK (average_rating BETWEEN 0 AND 5)
);

CREATE INDEX idx_restaurants_owner ON restaurants(owner_id);

CREATE INDEX idx_restaurants_city_active
    ON restaurants(city, is_active, is_approved)
    WHERE is_active = TRUE AND is_approved = TRUE;

CREATE INDEX idx_restaurants_rating ON restaurants(average_rating DESC)
    WHERE is_active = TRUE AND is_approved = TRUE;

CREATE INDEX idx_restaurants_name_trgm
    ON restaurants USING GIN (name gin_trgm_ops);

CREATE INDEX idx_restaurants_location ON restaurants(latitude, longitude);

CREATE TRIGGER  trigger_restaurants_updated_at
    BEFORE UPDATE ON restaurants
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN restaurants.min_order_paise IS 'Stored in paise (1/100 rupee) to avoid float precision issues';
COMMENT ON COLUMN restaurants.average_rating 
IS 'Denormalized from reviews. Updated by trigger on review insert/update/delete';