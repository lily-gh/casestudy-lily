--liquibase formatted sql
-- changeset lily:05
-- comment Populate 'tenant' table
INSERT INTO tenant (id, name, created_at) VALUES
(1,'enterprise-all-inclusive.com',NOW()),
(2,'betrieballes-inklusive.de',NOW());
--rollback delete from tenant;

-- changeset lily:06
-- comment Populate 'customer' table
INSERT INTO customer (id, tenant_id, external_id, created_at) VALUES
(1, 1,100,NOW()),
(2, 1,101,NOW()),
(3, 2,200,NOW());
--rollback delete from customer;

-- changeset lily:07
-- comment Populate 'transaction' table
INSERT INTO transaction (customer_id, amount, type, created_at, updated_at) VALUES
-- customer 1
(1, 500.00, 'PAY_IN', NOW(), null),
(1, 500.00, 'PAY_IN', NOW(), null),
-- customer 2
(2, 250.00, 'PAY_IN', NOW(), null),
(2, 500.00, 'PAY_IN', NOW(), null),
(2, -50.00, 'PAY_OUT', NOW(), null),
-- customer 3
(3, 1000.00, 'PAY_IN', NOW(), null),
(3, 1300.00, 'VOIDED', NOW(), NOW()),
(3, 200.00, 'VOIDED', NOW(), NOW()),
(3, 700.00, 'PAY_IN', NOW(), null),
(3, 500.00, 'PAY_IN', NOW(), null),
(3, -200.00, 'PAY_OUT', NOW(), null),
(3, -700.00, 'VOIDED', NOW(), NOW());
--rollback delete from transaction;

-- changeset lily:08
-- comment Populate 'customer_balance' table
INSERT INTO customer_balance (customer_id, balance, created_at) VALUES
(1, 1000.00,NOW()),
(2, 700.00,NOW()),
(3, 2000.00,NOW());
--rollback delete from customer_balance;
