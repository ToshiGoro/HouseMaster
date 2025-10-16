CREATE TABLE IF NOT EXISTS flats
(
    id          UUID PRIMARY KEY,
    flat_number SMALLINT NOT NULL,
    house_id    UUID     NOT NULL,
    updated_at  TIMESTAMP,

    UNIQUE (flat_number, house_id)
);

CREATE RULE prevent_flat_number_update AS
    ON UPDATE TO flats
    WHERE OLD.flat_number IS DISTINCT FROM NEW.flat_number
    DO INSTEAD NOTHING;

CREATE RULE prevent_house_id_update AS
    ON UPDATE TO flats
    WHERE OLD.house_id IS DISTINCT FROM NEW.house_id
    DO INSTEAD NOTHING;

COMMENT ON RULE prevent_flat_number_update ON flats IS 'Запрещает изменение номера квартиры';
COMMENT ON RULE prevent_house_id_update ON flats IS 'Запрещает изменение привязки к дому';