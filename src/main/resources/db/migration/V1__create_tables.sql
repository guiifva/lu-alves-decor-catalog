CREATE TABLE themes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    theme_id BIGINT NOT NULL,
    CONSTRAINT fk_theme FOREIGN KEY(theme_id) REFERENCES themes(id)
);
