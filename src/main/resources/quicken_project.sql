-- quicken_project.sql
-- Combined schema + data for simplified internship project (H2-compatible)

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
);

INSERT INTO accounts (id, name, description) VALUES
  (1, 'Personal Finances 2024', 'Personal income and household expenses'),
  (2, 'Small Business 2024', 'Simple small business cashflow'),
  (3, 'Student Budget 2024', 'Student income, loans, and living expenses');

INSERT INTO transactions (id, account_id, date, amount, description) VALUES
  (1001,1,'2024-01-01',3500.00,'Monthly salary'),
  (1002,1,'2024-01-02',-1500.00,'January rent'),
  (1003,1,'2024-01-05',-220.45,'Groceries - supermarket'),
  (1004,1,'2024-01-06',-45.90,'Dinner out'),
  (1005,1,'2024-01-10',-95.30,'Electricity and water'),
  (1006,1,'2024-01-15',400.00,'Freelance design gig'),
  (1007,1,'2024-01-18',-180.10,'Groceries'),
  (1008,1,'2024-01-20',-60.00,'Takeout'),
  (1009,1,'2024-02-01',3500.00,'Monthly salary'),
  (1010,1,'2024-02-02',-1500.00,'February rent'),
  (1011,1,'2024-02-05',-210.30,'Groceries'),
  (1012,1,'2024-02-08',-90.75,'Utilities'),

  (2001,2,'2024-01-03',5000.00,'Online store sales'),
  (2002,2,'2024-01-05',-1200.00,'Office rent'),
  (2003,2,'2024-01-07',-350.75,'Office supplies'),
  (2004,2,'2024-01-12',1800.00,'Consulting'),
  (2005,2,'2024-01-20',-425.50,'Travel'),
  (2006,2,'2024-02-02',6200.00,'Retail sales'),
  (2007,2,'2024-02-05',-1200.00,'Office rent'),
  (2008,2,'2024-02-08',-280.40,'Supplies'),
  (2009,2,'2024-02-15',-510.00,'Conference'),
  (2010,2,'2024-02-18',2200.00,'Consulting'),

  (3001,3,'2024-01-04',600.00,'Part-time job'),
  (3002,3,'2024-01-05',-700.00,'January rent'),
  (3003,3,'2024-01-06',-120.25,'Groceries'),
  (3004,3,'2024-01-10',-85.90,'Books'),
  (3005,3,'2024-01-12',-40.00,'Movie'),
  (3006,3,'2024-01-15',1000.00,'Scholarship'),
  (3007,3,'2024-01-18',-110.10,'Groceries'),
  (3008,3,'2024-02-01',2000.00,'Student loan'),
  (3009,3,'2024-02-05',-700.00,'February rent'),
  (3010,3,'2024-02-09',-55.75,'Concert');
