CREATE TABLE IF NOT EXISTS position_types
(
    id           UUID PRIMARY KEY,
    position_type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS positions
(
    id               UUID PRIMARY KEY,
    person_id        UUID NOT NULL,
    position_type_id UUID NOT NULL,
    house_id         UUID NOT NULL,

    UNIQUE (person_id, position_type_id, house_id),

    CONSTRAINT fk_position_person
        FOREIGN KEY (person_id)
            REFERENCES persons(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_position_type
        FOREIGN KEY (position_type_id)
            REFERENCES position_types(id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_position_house
        FOREIGN KEY (house_id)
            REFERENCES houses(id)
            ON DELETE CASCADE
);
