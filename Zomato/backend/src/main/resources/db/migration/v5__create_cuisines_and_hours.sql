CREATE TABLE cuisines (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    icon_url TEXT
);

CREATE TABLE restaurant_cuisines(
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    cuisine_id  INTEGER NOT NULL REFERENCES cuisines(id) ON DELETE CASCADE,
    PRIMARY KEY (restaurant_id , cuisine_id)
);

CREATE INDEX idx_restaurant_cuisines_cuisine ON restaurant_cuisines(cuisine_id);

INSERT INTO cuisines (name)  VALUES ('North India'),('South India'),('Chinese'),('Pizza'),('Burgers'),('Biryani'),('Desserts'),('Beverages'),('Sandwiches'),('Fast Food'),('Healthy'),('Continental'),('Thai'),('Mexican'),('Italian'),('Seafood'),('Rolls');

CREATE TABLE restaurant_hours (
    id SERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    day_of_week SMALLINT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    is_closed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_hours_order CHECK (opening_time < closing_time),
    UNIQUE (restaurant_id , day_of_week)
);

CREATE INDEX idx_restaurant_hours_restaurant ON restaurant_hours(restaurant_id);

COMMENT ON COLUMN restaurant_hours.day_of_week IS '0=Sunday, 1=Monday, ... , 6=Saturday';
