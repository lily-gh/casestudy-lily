--liquibase formatted sql
-- changeset lily:01
-- comment Create 'tenant' table
CREATE TABLE tenant
  (
     id         SERIAL NOT NULL,
     name       VARCHAR NOT NULL,
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP,
     PRIMARY KEY(id)
  )
--rollback drop table tenant

-- changeset lily:02
-- comment Create 'customer' table
CREATE TABLE customer
  (
     id          SERIAL NOT NULL,
     tenant_id   INT NOT NULL,
     external_id INT NOT NULL,
     created_at  TIMESTAMP NOT NULL,
     updated_at  TIMESTAMP,
     PRIMARY KEY(id),
     UNIQUE (tenant_id, external_id),
     FOREIGN KEY (tenant_id) REFERENCES tenant(id)
  )
--rollback drop table customer

-- changeset lily:03
-- comment Create 'customer_balance' table
CREATE TABLE customer_balance
  (
     id          SERIAL NOT NULL,
     customer_id INT NOT NULL,
     balance     DECIMAL(10, 2),
     created_at  TIMESTAMP NOT NULL,
     updated_at  TIMESTAMP,
     PRIMARY KEY (id),
     UNIQUE (customer_id),
     FOREIGN KEY (customer_id) REFERENCES customer(id)
  )
--rollback drop table customer_balance

-- changeset lily:04
-- comment Create 'transaction' table
CREATE TABLE transaction
  (
     id          SERIAL NOT NULL,
     customer_id INT NOT NULL,
     amount      DECIMAL(10, 2) NOT NULL,
     type        VARCHAR NOT NULL,
     created_at  TIMESTAMP NOT NULL,
     updated_at  TIMESTAMP,
     PRIMARY KEY (id),
     FOREIGN KEY (customer_id) REFERENCES customer(id)
  )
--rollback drop table transaction
