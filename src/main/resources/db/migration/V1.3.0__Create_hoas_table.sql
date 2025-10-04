CREATE TABLE IF NOT EXISTS hoas
(
    id               UUID PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    creation_date    DATE,
    liquidation_date DATE,
    updated_at       TIMESTAMP
);
