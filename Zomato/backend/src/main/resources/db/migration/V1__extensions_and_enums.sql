-- EXTENSIONS

-- This line ensures you database can generate unique IDs
--(UUIDs) using built-in functions like uuid_generate_v4()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"
-- This line enables security + encryption + UUID generation features in postgresql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
-- This line enables smart text searching and typo-tolerant queries in postgresSQL
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
-- This line enables accent-insensitive text processing in postgresql 
--So searching "cafe" also finds "café"
CREATE EXTENSION IF NOT EXISTS "unaccent";

-- CUSTOM ENUM TYPES
CREATE TYPE user_role AS ENUM(
    'CUSTORMER',
    'RESTAURANT_OWNER',
    'DELIVERY_PARTNER',
    'ADMIN'
);

CREATE TYPE order_status AS ENUM(
    'PENDING',
    'CONFIRMED',
    'PREPARING',
    'READY_FOR_PICKUP',
    'PICKED_UP',
    'OUT_FOR_DELIVERY',
    'DELIVERED',
    'CANCELLED',
    'REFUNDED'
);

CREATE TYPE payment_status AS ENUM (
    'PENDING',
    'SUCCESS',
    'FAILED',
    'REFUNDED'
);

CREATE TYPE payment_method AS ENUM (
    'CASH_ON_DELIVERY',
    'UPI',
    'CARD',
    'WALLET',
    'NET_BANKING'
);

CREATE TYPE delivery_partner_status AS ENUM (
    'OFFLINE',
    'AVAILABLE',
    'BUSY'
);

CREATE TYPE food_type AS ENUM (
    'VEG',
    'NON_VEG',
    'EGG'
);
