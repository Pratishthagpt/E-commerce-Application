CREATE TABLE cart
(
    id          BINARY(16)   NOT NULL,
    user_id     VARCHAR(255) NULL,
    total_price INT          NOT NULL,
    total_items INT          NOT NULL,
    created_at  datetime     NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_cart_items
(
    cart_id       BINARY(16) NOT NULL,
    cart_items_id BINARY(16) NOT NULL
);

CREATE TABLE cart_item
(
    id            BINARY(16)   NOT NULL,
    product_id    VARCHAR(255) NULL,
    quantity      INT          NOT NULL,
    price         INT          NOT NULL,
    item_added_at datetime     NULL,
    CONSTRAINT pk_cartitem PRIMARY KEY (id)
);

ALTER TABLE cart_cart_items
    ADD CONSTRAINT uc_cart_cart_items_cartitems UNIQUE (cart_items_id);

ALTER TABLE cart_cart_items
    ADD CONSTRAINT fk_carcarite_on_cart FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_cart_items
    ADD CONSTRAINT fk_carcarite_on_cart_item FOREIGN KEY (cart_items_id) REFERENCES cart_item (id);