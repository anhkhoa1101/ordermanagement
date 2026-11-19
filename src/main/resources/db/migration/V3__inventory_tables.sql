-- V3__inventory_tables.sql
CREATE TABLE inventory.products (
    id     UUID PRIMARY KEY,
    name   VARCHAR(255)  NOT NULL,
    price  NUMERIC(15,2) NOT NULL CHECK (price >= 0),
    stock  INT           NOT NULL CHECK (stock >= 0)
);