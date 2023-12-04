--liquibase formatted sql
-- changeset lily:09
-- comment Create 'audit_log' table
CREATE TABLE audit_log
  (
     id             SERIAL NOT NULL,
     type           VARCHAR NOT NULL,
     tenant_id      INT NOT NULL,
     external_id    INT NOT NULL,
     customer_id    INT NOT NULL,
     transaction_id INT NOT NULL,
     remote_address VARCHAR NOT NULL,
     created_at     TIMESTAMP NOT NULL,
     PRIMARY KEY(id)
  );

CREATE INDEX idx_audit_log_customer_id
 ON transaction (customer_id);
--rollback drop table audit_log
