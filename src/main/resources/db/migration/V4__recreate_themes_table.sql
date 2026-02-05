DROP TABLE IF EXISTS theme_items;
DROP TABLE IF EXISTS themes;

CREATE TABLE themes (
    id UUID PRIMARY KEY,
    slug VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    age_group VARCHAR(50) NOT NULL,
    price_min NUMERIC(15, 2),
    price_max NUMERIC(15, 2),
    images TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- CREATE UNIQUE INDEX uq_themes_name_ci ON themes ((lower(name)));
-- Functional index commented out due to H2 syntax incompatibility in tests.
-- Application layer handles uniqueness check.

CREATE TABLE theme_items (
    theme_id UUID NOT NULL,
    item_id UUID NOT NULL,
    PRIMARY KEY (theme_id, item_id),
    CONSTRAINT fk_theme FOREIGN KEY (theme_id) REFERENCES themes(id) ON DELETE CASCADE,
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);
