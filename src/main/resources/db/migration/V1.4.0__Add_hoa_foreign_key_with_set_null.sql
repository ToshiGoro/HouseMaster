ALTER TABLE houses
    ADD CONSTRAINT fk_house_hoa
        FOREIGN KEY (hoa_id)
            REFERENCES hoas(id)
            ON DELETE SET NULL;