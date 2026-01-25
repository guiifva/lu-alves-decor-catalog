DROP TABLE IF EXISTS decor_item;

ALTER TABLE decor_items RENAME TO items;

ALTER INDEX IF EXISTS decor_items_pkey RENAME TO items_pkey;
ALTER INDEX IF EXISTS decor_items_sku_key RENAME TO items_sku_key;
