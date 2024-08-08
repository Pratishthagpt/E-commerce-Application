CREATE TABLE payment
(
    id             BINARY(16)   NOT NULL,
    order_id       VARCHAR(255) NULL,
    reference_id   VARCHAR(255) NULL,
    payment_status TINYINT NULL,
    payment_link   VARCHAR(255) NULL,
    amount         INT NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);