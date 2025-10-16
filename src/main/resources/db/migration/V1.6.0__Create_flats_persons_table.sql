CREATE TABLE IF NOT EXISTS flat_person (
                                           id UUID PRIMARY KEY,
                                           flat_id UUID NOT NULL,
                                           person_id UUID NOT NULL,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                           FOREIGN KEY (flat_id) REFERENCES flats(id) ON DELETE CASCADE,
                                           FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE,

                                           UNIQUE (flat_id, person_id)
);