CREATE TABLE CUSTOMER (
    IDT_CUSTOMER INT,
    NAM_CUSTOMER VARCHAR(255),
    COD_PASSWORD VARCHAR(255)
);

CREATE TABLE CUSTOMER_ROLE(
    IDT_CUSTOMER INT,
    IND_ROLE VARCHAR(255)
);

INSERT INTO customer VALUES (1, 'admin', 'admin');
INSERT INTO customer_role VALUES (1, 'admin');

INSERT INTO customer VALUES (2, 'user','user');
INSERT INTO customer_role VALUES (2, 'user');

INSERT INTO customer VALUES (3, 'master','master');
INSERT INTO customer_role VALUES (3, 'master');
INSERT INTO customer_role VALUES (3, 'user');
INSERT INTO customer_role VALUES (3, 'admin');
