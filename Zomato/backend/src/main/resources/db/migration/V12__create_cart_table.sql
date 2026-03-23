CREATE TABLE cart_items(

    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    restaurant_id   BIGINT NOT NULL REFERENCES restaurants(id) on DELETE CASCADE,
    menu_item_id    BIGINT NOT NULL REFERENCES menu_items(id) On DELETE CASCADE,
    quantity        SMALLINT NOT NULL DEFAULT 1 CHECK (quantity > 0),

    customization_note TEXT,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    added_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    UNIQUE (user_id, menu_item_id)

);

CREATE TRIGGER trigger_cart_items_updated_at
    BEFORE UPDATE ON cart_items
    FOR EACH row
    EXECUTE FUNCTION update_updated_at_column();