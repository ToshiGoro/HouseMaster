CREATE TABLE IF NOT EXISTS houses
(
    id               UUID PRIMARY KEY,
    address          VARCHAR(200) NOT NULL,
    hoa_id           UUID,
    living_area      DECIMAL(8, 2),
    num_of_floors    SMALLINT,
    num_of_sections  SMALLINT,
    num_of_entrances SMALLINT,
    num_of_flats     SMALLINT,
    num_of_offices   SMALLINT,
    updated_at       TIMESTAMP
);
