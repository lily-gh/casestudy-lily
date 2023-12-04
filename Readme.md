# Case Study - Enterprise All Inclusive

## Steps to run the program locally with Docker
1. clone this repository
2. from the repository folder, run:
    ```bash
    docker-compose -f docker-compose.yml up -d --build
    ```
    This should create and start a docker container for the database and one for the app itself, which should be running on port `8080`:

    ![Docker Containers Running](img/containers.png)
3. run `docker-compose down` when you want to stop and remove the docker containers.


## Entity Relationship Diagram
These are the entities used in the app:
![Entity Relationship Diagram](img/er-diagram.png)

Since multiple tenants might use the same customer number in their external systems, we call the customer number `external_id` on our side, and have our own internal `id` which is automatically generated. This helps us avoid issues when multiple tenants have customers with the same "customer number" on their systems and also help us avoid duplicating both `tenant_id` and `external_id` in related tables.


## API
To easily use the api, open [Postman](https://www.postman.com/downloads/) and import the included Postman collection file [postman_collection.json](postman_collection.json) to try out the endpoints.


### book money
**`POST tenants/{tenantId}/customers/{customerNumber}/book-money`**

Request params:
 - **tenantId**: the tenant ID as stored in the database.
 - **customerNumber**: the customer number as the tenant sees it on their side (stored as `external_id` on our side).

Request body:
```json
{
    "amount": 37.50 // amount to be booked
}
```

Response body:
```json
{
    "transactionId": 34, // id for the newly created transaction, can be used to void the transaction
    "balance": 1000.00 // updated customer balance
}
```
This API can be used to book positive or negative amounts.


### void transaction
**`PATCH tenants/{tenantId}/customers/{customerNumber}/void-transaction`**

Request params:
 - **tenantId**: the tenant ID as stored in the database.
 - **customerNumber**: the customer number as the tenant sees it on their side (stored as `external_id` on our side).

Request body:
```json
{
    "transactionId": 20 // transaction to be voided
}
```

Response body:
```json
{
    "balance": 1000.00 // updated customer balance after voiding the transaction
}
```


### fetch recent transactions by tenant and customer number
**`GET customercare/tenants/{tenantId}/customers/{customerNumber}/transactions?page=0&size=3`**

Request params:
 - **tenantId**: the tenant ID as stored in the database.
 - **customerNumber**: the customer number as the tenant sees it on their side (stored as `external_id` on our side).
 - **page**: which page to start listing the records from.
 - **size**: size of the page returned.

Response body:
```json
{
    "transactions": [
        {
            "id": 1,
            "customerId": 1,
            "amount": 500.00,
            "type": "PAY_IN",
            "createdAt": "2023-12-04T19:12:11.571908",
            "updatedAt": null
        },
        {
            "id": 2,
            "customerId": 1,
            "amount": 500.00,
            "type": "PAY_IN",
            "createdAt": "2023-12-04T19:12:11.571908",
            "updatedAt": null
        },
        {
            "id": 24,
            "customerId": 1,
            "amount": 37.50,
            "type": "PAY_IN",
            "createdAt": "2023-12-04T19:12:13.94461",
            "updatedAt": null
        }
    ],
    "page": 0,
    "totalElements": 5
}
```


## Seed data
Some seed data is already available for use with the app:

_Note: some columns have been omitted (e.g. `created_at`, `updated_at`)_

<details open>
  <summary><h3>tenant table</h3></summary>

| id  | name                         |
| --- | ---------------------------- |
| 1   | enterprise-all-inclusive.com |
| 2   | betrieballes-inklusive.de    |
</details>


<details open>
  <summary><h3>customer table</h3></summary>

| id  | tenant_id | external_id |
| --- | --------- | ----------- |
| 1   | 1         | 100         |
| 2   | 1         | 101         |
| 3   | 2         | 100         |
| 4   | 2         | 101         |
| 5   | 2         | 200         |
</details>


<details open>
  <summary><h3>customer_balance table</h3></summary>

| customer_id | balance |
| ----------- | ------- |
| 1           | 1000.00 |
| 2           | 700.00  |
| 3           | 2000.00 |
| 4           | 1200.00 |
| 5           | 250.00  |
</details>


<details>
  <summary><h3>transaction table</h3></summary>

| customer_id | amount  | type    |
| ----------- | ------- | ------- |
| 1           | 500.00  | PAY_IN  |
| 1           | 500.00  | PAY_IN  |
| 2           | 250.00  | PAY_IN  |
| 2           | 500.00  | PAY_IN  |
| 2           | -50.00  | PAY_OUT |
| 3           | 1000.00 | PAY_IN  |
| 3           | 1300.00 | VOIDED  |
| 3           | 200.00  | VOIDED  |
| 3           | 700.00  | PAY_IN  |
| 3           | 500.00  | PAY_IN  |
| 3           | -200.00 | PAY_OUT |
| 3           | -700.00 | VOIDED  |
| 4           | 200.00  | VOIDED  |
| 4           | 700.00  | PAY_IN  |
| 4           | 500.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | 100.00  | PAY_IN  |
| 5           | -450.00 | PAY_OUT |
</details>


## Notes and improvement ideas 💡
 - Sequential IDs have been used as the primary keys for the tables to make it simpler to visualize and play around with in Postman, on a production environment I would opt for using [UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier) instead, as they prevent malicious users from guessing the IDs and abusing the API.

 - Secure endpoints with authentication (e.g. JWT token). With added security, it would be possible to also populate the `audit_log` table with user data such as email and username instead of only the request parameters and user's remote address. _(I might still try to add JWT authentication through the week)_