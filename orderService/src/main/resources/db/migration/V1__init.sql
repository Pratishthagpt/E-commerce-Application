CREATE TABLE address
(
    id       BINARY(16)   NOT NULL,
    user_id  VARCHAR(255) NULL,
    house_no VARCHAR(255) NULL,
    city     VARCHAR(255) NULL,
    state    VARCHAR(255) NULL,
    country  VARCHAR(255) NULL,
    pincode  VARCHAR(255) NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE `order`
(
    id            BINARY(16)   NOT NULL,
    `description` VARCHAR(255) NULL,
    address_id    BINARY(16)   NULL,
    order_status  TINYINT NULL,
    payment_id    VARCHAR(255) NULL,
    created_on    datetime NULL,
    total_price   INT NOT NULL,
    quantity      INT NOT NULL,
    user_id       VARCHAR(255) NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id         BINARY(16)   NOT NULL,
    product_id VARCHAR(255) NULL,
    quantity   INT NOT NULL,
    order_id   BINARY(16)   NULL,
    created_on datetime NULL,
    CONSTRAINT pk_orderitem PRIMARY KEY (id)
);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES `order` (id);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);