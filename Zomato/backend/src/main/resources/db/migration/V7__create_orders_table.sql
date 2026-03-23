CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number    VARCHAR(20) not null UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES users(id),
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id),

    delivery_address_line1 VARCHAR(255) NOT NULL,
    delivery_address_line2 VARCHAR(255) ,
    delivery_landmark      VARCHAR(255) ,
    delivery_city          VARCHAR(255) NOT NULL,
    delivery_state         VARCHAR(100) NOT NULL,
    delivery_pincode       VARCHAR(100) NOT NULL,
    delivery_latitude      DECIMAL(10, 8),
    delivery_longitude     DECIMAL(11,8),

    subtotal_paise INTEGER NOT NULL CHECK (subtotal_paise >= 0),
    delivery_free_paise   INTEGER NOT NULL DEFAULT 0,
    tax_paise             INTEGER NOT NULL DEFAULT 0,
    discount_paise        INTEGER NOT NULL DEFAULT 0,
    total_paise           INTEGER NOT NULL CHECK(total_paise>=0),
    coupon_code           VARCHAR(50),

    status  order_status NOT NULL DEFAULT 'PENDING',
    special_instructions TEXT,
    
    placed_at             TIMESTAMPTZ not null DEFAULT now(),
    cofirmed_at           TIMESTAMPTZ ,
    preparing_at          TIMESTAMPTZ,
    ready_at              TIMESTAMPTZ,
    picked_up_at          TIMESTAMPTZ,
    delivered_at          TIMESTAMPTZ,
    cancelled_at          TIMESTAMPTZ,

    cancellation_reason     TEXT,
    cancelled_by            VARCHAR(20),
    estimated_delivery_minutes  INTEGER,

    created_at   TIMESTAMPTZ not NUll DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_total_calculation CHECK (
        total_paise = subtotal_paise + delivery_free_paise +tax_paise - discount_paise
    )
);

CREATE INDEX idx_orders_customer
    ON orders(customer_id , placed_at DESC);

CREATE INDEX idx_orders_restaurant_status
    ON orders(restaurant_id ,status ,placed_at DESC);

CREATE  INDEX idx_orders_active_status
    ON orders(status)
    WHERE  status NOT IN ('DELIVERED', 'CANCELLED' , 'REFUNDED');

CREATE TRIGGER tirgger_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE order_items(

    id      BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_items_id   BIGINT NOT NULL REFERENCES menu_items(id),

    item_name   VARCHAR(255) NOT NULL,
    item_price_paise INTEGER NOT NULL,
    food_type food_type NOT NULL ,

    quantity SMALLINT NOT NULL CHECK (quantity > 0),
    line_total_paise INTEGER NOT NULL,
    customization_note TEXT,

    CONSTRAINT chk_line_total CHECK (line_total_paise = quantity * item_price_paise)
);

CREATE INDEX idx_orders_itmes_order ON order_items(order_id);

CREATE INDEX idx_orders_itmes_menu_item ON order_items(menu_items_id);

COMMENT ON COLUMN order_items.item_name IS 'Snapshot - copied at order time. Menu item name may change later';
COMMENT ON COLUMN order_items.item_price_paise IS 'Snapshot -price at time of ordering , not current price';