CREATE TABLE decor_item (
    id UUID PRIMARY KEY,
    sku TEXT UNIQUE,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    description TEXT,
    quantity_available INTEGER NOT NULL CHECK (quantity_available >= 0),
    unit TEXT,
    replacement_value NUMERIC(12,2),
    rental_price NUMERIC(12,2),
    photos TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_decor_item_name_ci ON decor_item (name);
CREATE INDEX ix_decor_item_category ON decor_item (category);
