CREATE TABLE  reviews(
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT  NOT NULL UNIQUE REFERENCES ORDERS(id),
    customer_id BIGINT NOT NULL REFERENCES users(id),
    restaurant_id BIGINT not null REFERENCES restaurants(id),

    rating Smallint Not null CHECK (rating between 1 and 5),

    commnet text,

    food_rating     SMALLINT CHECK (food_rating BETWEEN 1 AND 5),
    delivery_rating SMALLINT CHECK (delivery_rating BETWEEN 1 and 5),
    packaging_rating SMALLINT CHECK (packaging_rating BETWEEN 1 AND 5),

    image_urls   JSONB DEFAULT '[]' :: JSONB,
    owner_reply TEXT,
    owner_replied_at TIMESTAMPTZ,
    is_visible  BOOLEAN NOT NULL DEFAULT TRUE,

    created_at   TIMESTAMPTZ not null DEFAULT NOW(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()

);

CREATE INDEX idx_review_restaurant
    ON reviews(restaurant_id, created_at DESC)
    WHERE is_visible = TRUE;

CREATE INDEX idx_review_customer ON reviews(customer_id);

CREATE TRIGGER trigger_reviews_updated_at
    BEFORE UPDATE ON reviews
    for each row
    EXECUTE FUNCTION update_updated_at_column();

CREATE OR REPLACE FUNCTION update_restaurant_rating()
RETURNS TRIGGER AS $$
DECLARE
    v_restaurant_id     BIGINT;
    v_avg_rating        DECIMAL(3,2);
    V_total            INTEGER;

BEGIN
    IF TG_OP = 'DELETE' THEN
        v_restaurant_id := OLD.restaurant_id;
    ELSE 
        v_restaurant_id := NEW.restaurant_id;
    END IF;

    SELECT
        COALESCE(AVG(RATING),0),
        COUNT(*)

    INTO v_avg_rating , V_total
    FROM reviews
    WHERE restaurant_id = v_restaurant_id
        AND is_visible = TRUE;

    UPDATE restaurants
    SET
            average_rating = ROUND(v_avg_rating,2),
            total_ratings = V_total
    WHERE id = v_restaurant_id;
    RETURN NULL;
END;

$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_restaurant_rating
    AFTER INSERT OR UPDATE OR DELETE ON reviews
    FOR EACH row
    EXECUTE FUNCTION update_restaurant_rating();