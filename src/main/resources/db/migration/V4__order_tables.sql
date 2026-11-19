-- V4
CREATE TABLE ordering.orders (
    id          UUID PRIMARY KEY,
    user_id     UUID           NOT NULL,   -- không FK sang schema users
    status      VARCHAR(20)    NOT NULL,   -- PENDING | CONFIRMED | CANCELLED
    total       NUMERIC(15,2)  NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE ordering.order_items (
    id          UUID PRIMARY KEY,
    order_id    UUID           NOT NULL REFERENCES ordering.orders(id),
    product_id  UUID           NOT NULL,
    quantity    INT            NOT NULL CHECK (quantity > 0),
    unit_price  NUMERIC(15,2)  NOT NULL
);