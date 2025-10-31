CREATE TABLE themes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE theme_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    theme_id BIGINT NOT NULL,
    CONSTRAINT fk_theme FOREIGN KEY(theme_id) REFERENCES themes(id)
);

CREATE TABLE decor_items (
    id UUID PRIMARY KEY,
    sku VARCHAR(32) UNIQUE,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    quantity_available INT NOT NULL,
    unit VARCHAR(32),
    replacement_value NUMERIC(15, 2),
    rental_price NUMERIC(15, 2),
    photos TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
