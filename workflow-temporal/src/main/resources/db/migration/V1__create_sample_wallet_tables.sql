create table investor (
  id varchar(100) not null,
  profile varchar(30) not null,
  constraint pk_investor primary key (id)
);

create table wallet (
  id varchar(100) not null,
  investor_id varchar(100) not null,
  status varchar(30) not null,
  created_at timestamptz not null,
  ready_at timestamptz,
  aborted_at timestamptz,
  constraint pk_wallet primary key (id),
  constraint fk_wallet__investor_id__investor foreign key (investor_id) references investor (id)
);

create index ix_wallet__investor_id__created_at
  on wallet (investor_id, created_at);

create table investment (
  id varchar(100) not null,
  wallet_id varchar(100) not null,
  investor_id varchar(100) not null,
  base_investment_id varchar(100) not null,
  profile varchar(30) not null,
  created boolean not null,
  constraint pk_investment primary key (id),
  constraint fk_investment__wallet_id__wallet foreign key (wallet_id) references wallet (id),
  constraint fk_investment__investor_id__investor foreign key (investor_id) references investor (id)
);

create index ix_investment__wallet_id
  on investment (wallet_id);

create index ix_investment__investor_id__profile
  on investment (investor_id, profile);

create table financial_event_candidate (
  id varchar(100) not null,
  investment_id varchar(100) not null,
  status varchar(30) not null,
  processed boolean not null,
  attempts integer not null,
  processed_at timestamptz,
  constraint pk_financial_event_candidate primary key (id),
  constraint fk_financial_event_candidate__investment_id__investment foreign key (investment_id) references investment (id)
);

create index ix_financial_event_candidate__investment_id
  on financial_event_candidate (investment_id);

create index ix_financial_event_candidate__status__processed
  on financial_event_candidate (status, processed);
