CREATE TABLE IF NOT EXISTS persons
(
    id          UUID PRIMARY KEY,
    first_name  VARCHAR(30) NOT NULL,
    second_name VARCHAR(30),
    last_name   VARCHAR(50) NOT NULL,
    gender      BOOLEAN,
    birth_date  DATE,
    updated_at  TIMESTAMP
);
