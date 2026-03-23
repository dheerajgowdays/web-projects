-- =========================
-- DELIVERY PARTNERS
-- =========================

CREATE TABLE delivery_partners (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,

    vehicle_type VARCHAR(50) NOT NULL,
    vehicle_number VARCHAR(20),
    license_number VARCHAR(50),
    aadhar_number VARCHAR(12),

    status delivery_partner_status NOT NULL DEFAULT 'OFFLINE',

    current_latitude DECIMAL(10,8),
    current_longitude DECIMAL(11,8),
    location_updated_at TIMESTAMPTZ,

    total_deliveries INTEGER NOT NULL DEFAULT 0,
    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    city VARCHAR(100),

    id_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_delivery_partners_available
    ON delivery_partners(city, status)
    WHERE status = 'AVAILABLE' AND is_active = TRUE;

CREATE INDEX idx_delivery_partners_location
    ON delivery_partners(current_latitude, current_longitude)
    WHERE status = 'AVAILABLE';

CREATE TRIGGER trigger_delivery_partners_updated_at
    BEFORE UPDATE ON delivery_partners
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =========================
-- DELIVERY ASSIGNMENTS
-- =========================

CREATE TABLE delivery_assignments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    delivery_partner_id BIGINT NOT NULL REFERENCES delivery_partners(id),

    assigned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    accepted_at TIMESTAMPTZ,
    rejected_at TIMESTAMPTZ,
    picked_up_at TIMESTAMPTZ,
    delivered_at TIMESTAMPTZ,

    delivery_latitude DECIMAL(10,8),
    delivery_longitude DECIMAL(11,8),

    partner_rating SMALLINT CHECK (partner_rating BETWEEN 1 AND 5),

    distance_meters INTEGER,
    earnings_paise INTEGER DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_delivery_assignments_partner
    ON delivery_assignments(delivery_partner_id, assigned_at DESC);

CREATE INDEX idx_delivery_assignments_order
    ON delivery_assignments(order_id);

CREATE TRIGGER trigger_delivery_assignments_updated_at
    BEFORE UPDATE ON delivery_assignments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();