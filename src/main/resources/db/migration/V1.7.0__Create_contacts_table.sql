CREATE TABLE IF NOT EXISTS contact_types
(
    id           UUID PRIMARY KEY,
    contact_type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS contacts
(
    id              UUID PRIMARY KEY,
    person_id       UUID         NOT NULL,
    contact_type_id UUID         NOT NULL,
    contact         VARCHAR(100) NOT NULL,

    UNIQUE (person_id, contact_type_id, contact),

    CONSTRAINT fk_contact_person
        FOREIGN KEY (person_id)
            REFERENCES persons(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_contact_type
        FOREIGN KEY (contact_type_id)
            REFERENCES contact_types(id)
            ON DELETE RESTRICT
);