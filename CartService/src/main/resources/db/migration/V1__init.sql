CREATE TABLE cart
(
    id          BINARY(16)   NOT NULL,
    user_id     VARCHAR(255) NULL,
    total_price INT NOT NULL,
    total_items INT NOT NULL,
    created_at  datetime NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id            BINARY(16)   NOT NULL,
    cart          BINARY(16)   NULL,
    product_id    VARCHAR(255) NULL,
    quantity      INT NOT NULL,
    price         INT NOT NULL,
    item_added_at datetime NULL,
    CONSTRAINT pk_cartitem PRIMARY KEY (id)
);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_CART FOREIGN KEY (cart) REFERENCES cart (id);