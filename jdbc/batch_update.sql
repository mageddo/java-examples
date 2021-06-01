CREATE TABLE FINANCIAL_ACCOUNT (
	IDT_FINANCIAL_ACCOUNT VARCHAR(36) NOT NULL PRIMARY KEY,
	NUM_BALANCE NUMERIC(20, 2) NOT NULL DEFAULT 0
);

INSERT INTO financial_account VALUES 
('671782f5-650b-419d-aedb-20fc191b744e'),
('85426177-d8f7-4e22-8ba8-6686b122c379'),
('75fbce92-ffe3-48c1-a68d-b14e606994d1'),
('b51b9487-bded-49e4-8230-91b8063ca38e'),
('e5556ab7-a04e-491f-a175-2dba2c8893cc')



select
	fa.*, 
	('x'|| substring(idt_financial_account, 36)) hex,
	('x'|| substring(idt_financial_account, 36))::bit(4)::int * 10000 expected_amount 
from financial_account fa
order by idt_financial_account


update financial_account set num_balance = 0