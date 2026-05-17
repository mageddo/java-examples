create table INVESTOR (
  IDT_INVESTOR varchar(100) not null,
  IND_PROFILE varchar(30) not null,
  DAT_CREATED timestamp not null default timezone('UTC', clock_timestamp()),
  DAT_UPDATED timestamp,
  constraint INVESTOR_PK primary key (IDT_INVESTOR)
);

create table WALLET (
  IDT_WALLET varchar(100) not null,
  IDT_INVESTOR varchar(100) not null,
  IND_STATUS varchar(30) not null,
  DAT_READY timestamp,
  DAT_ABORTED timestamp,
  DAT_CREATED timestamp not null default timezone('UTC', clock_timestamp()),
  DAT_UPDATED timestamp,
  constraint WALLET_PK primary key (IDT_WALLET)
);

create index WALLET_IDX1
  on WALLET (IDT_INVESTOR, DAT_CREATED);

create table INVESTMENT (
  IDT_INVESTMENT varchar(100) not null,
  IDT_WALLET varchar(100) not null,
  IDT_INVESTOR varchar(100) not null,
  IDT_BASE_INVESTMENT varchar(100) not null,
  IND_PROFILE varchar(30) not null,
  FLG_CREATED boolean not null,
  DAT_CREATED timestamp not null default timezone('UTC', clock_timestamp()),
  DAT_UPDATED timestamp,
  constraint INVESTMENT_PK primary key (IDT_INVESTMENT)
);

create index INVESTMENT_IDX1
  on INVESTMENT (IDT_WALLET);

create index INVESTMENT_IDX2
  on INVESTMENT (IDT_INVESTOR, IND_PROFILE);

create table FINANCIAL_EVENT_CANDIDATE (
  IDT_FINANCIAL_EVENT_CANDIDATE varchar(100) not null,
  IDT_INVESTMENT varchar(100) not null,
  IND_STATUS varchar(30) not null,
  FLG_PROCESSED boolean not null,
  NUM_ATTEMPTS integer not null,
  DAT_PROCESSED timestamp,
  DAT_CREATED timestamp not null default timezone('UTC', clock_timestamp()),
  DAT_UPDATED timestamp,
  constraint FINANCIAL_EVENT_CANDIDATE_PK primary key (IDT_FINANCIAL_EVENT_CANDIDATE)
);

create index FINANCIAL_EVENT_CANDIDATE_IDX1
  on FINANCIAL_EVENT_CANDIDATE (IDT_INVESTMENT);

create index FINANCIAL_EVENT_CANDIDATE_IDX2
  on FINANCIAL_EVENT_CANDIDATE (IND_STATUS, FLG_PROCESSED);

alter table WALLET
  add constraint WALLET_FK1
  foreign key (IDT_INVESTOR)
  references INVESTOR (IDT_INVESTOR);

alter table INVESTMENT
  add constraint INVESTMENT_FK1
  foreign key (IDT_WALLET)
  references WALLET (IDT_WALLET);

alter table INVESTMENT
  add constraint INVESTMENT_FK2
  foreign key (IDT_INVESTOR)
  references INVESTOR (IDT_INVESTOR);

alter table FINANCIAL_EVENT_CANDIDATE
  add constraint FINANCIAL_EVENT_CANDIDATE_FK1
  foreign key (IDT_INVESTMENT)
  references INVESTMENT (IDT_INVESTMENT);
