CREATE TABLE CUSTOMER_BALANCE
(
  IDT_CUSTOMER_BALANCE UUID           NOT NULL PRIMARY KEY,
  COD_CUSTOMER         UUID           NOT NULL UNIQUE,
  NUM_BALANCE          NUMERIC(20, 2) NOT NULL,
  DAT_CREATED          TIMESTAMP      NOT NULL,
  DAT_UPDATED          TIMESTAMP      NOT NULL
);

CREATE TABLE CUSTOMER_BALANCE_HISTORY
(
  IDT_CUSTOMER_BALANCE_HISTORY UUID           NOT NULL PRIMARY KEY,
  IDT_CUSTOMER_BALANCE         UUID           NOT NULL,
  COD_CUSTOMER                 UUID           NOT NULL,
  NUM_BALANCE                  NUMERIC(20, 2) NOT NULL,
  DAT_CUSTOMER_BALANCE_UPDATED TIMESTAMP      NOT NULL,
  DAT_CREATED                  TIMESTAMP      NOT NULL
);


CREATE TABLE CUSTOMER_REGION
(
  IDT_CUSTOMER_REGION UUID        NOT NULL PRIMARY KEY,
  NAM_REGION          VARCHAR(10) NOT NULL,
  COD_CUSTOMER        UUID        NOT NULL
)
