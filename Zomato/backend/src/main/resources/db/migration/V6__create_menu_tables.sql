CREATE TABLE menu_categories (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    display_order SMALLINT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (restaurant_id , name)
);

CREATE INDEX idx_restaurant_categories_restaurant
    ON menu_categories(restaurant_id)
    WHERE is_active = TRUE;

CREATE TRIGGER trigger_menu_categories_updated_at
    BEFORE UPDATE ON menu_categories
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id)ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES menu_categories(id) on delete CASCADE,

    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url TEXT,

    price_paise INTEGER NOT NULL CHECK (price_paise > 0),
    original_price_paise INTEGER,
    food_type food_type NOT NULL DEFAULT 'VEG',
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    is_bestseller BOOLEAN NOT NULL DEFAULT FALSE,
    spice_level SMALLINT DEFAULT 0 CHECK (spice_level BETWEEN 0 AND 4),
    preparation_time_minutes SMALLINT,
    calories SMALLINT,
    display_order SMALLINT NOT NULL DEFAULT 0,

    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    total_rating INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ not NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_discount_price CHECK (
        original_price_paise IS NULL OR 
        original_price_paise > price_paise
    )
);

CREATE INDEX idx_menu_items_category ON menu_items (category_id);

CREATE INDEX idx_menu_items_restaurant
    on menu_items(restaurant_id, is_available)
    WHERE is_available = TRUE;

CREATE INDEX idx_menu_items_name_trgm
    ON menu_items USING GIN (name gin_trgm_ops);

CREATE TRIGGER trigger_menu_items_updated_at
    BEFORE UPDATE ON menu_items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN menu_items.price_paise IS 'Price in paise (1/100 of rupee). Divide by 100 to display in rupees';
COMMENT ON COLUMN menu_items.original_price_paise IS 'Pre-discount price. NULL means no discount active'