# Seed Data

## Run Seeds Files


### Recommended Seeding of Database (Safe & Atomic)

> Please note that the `buzzticket-db` is the container name from docker compose file. 
#### Step 1
```shell
docker cp seed buzzticket-db:/seed
```

> Please note that the names `BUZZTICKET` is the .env field `DB_USERNAME` and `buzzticket` is the .env field called `DB_NAME`. 
#### Step 2
```shell
docker exec -it buzzticket-db \
psql -U BUZZTICKET -d buzzticket -f /seed/run_seed.sql
```



### Unsafe Manual Seeding of Database (Not Recommended!)

#### Step 1

```shell
cat seed/v1_initial_setup.sql | docker exec -i buzzticket-db psql -U BUZZTICKET -d buzzticket

```
#### Step 2
```shell
cat seed/v2_adds_customers.sql | docker exec -i buzzticket-db psql -U BUZZTICKET -d buzzticket

```
#### Step 3
```shell
cat seed/v3_adds_staff_members.sql | docker exec -i buzzticket-db psql -U BUZZTICKET -d buzzticket
```

#### Step 4
```shell
cat seed/v4_adds_tickets_pending_status.sql | docker exec -i buzzticket-db psql -U BUZZTICKET -d buzzticket
```

-----

## Admin Login

| Partner     | Email             | Password   | Role   |
|-------------|------------------|------------|--------|
| Acme Corp   | admin@acme.com   | Secret@1   | ADMIN  |
| Beta Corp   | admin2@acme.com  | Secret@2   | ADMIN  |
| Zeta Corp   | admin3@acme.com  | Secret@3   | ADMIN  |

## Customer Login

| Partner     | Email               | Password     | Role      |
|-------------|---------------------|--------------|-----------|
| Acme Corp   | customer@acme.com   | Customer@1   | CUSTOMER  |
| Acme Corp   | customer2@acme.com  | Customer@2   | CUSTOMER  |
| Acme Corp   | customer3@acme.com  | Customer@3   | CUSTOMER  |
| Beta Corp   | customer1@beta.com  | Customer@1   | CUSTOMER  |
| Beta Corp   | customer2@beta.com  | Customer@2   | CUSTOMER  |
| Zeta Corp   | customer@zeta.com   | Customer@1   | CUSTOMER  |

## Additional Staff members

| Partner     | Email                      | Password     | Role     |
|-------------|----------------------------|--------------|----------|
| Acme Corp   | register.admin@acme.com    | Secret@1     | ADMIN    |
| Acme Corp   | support@acme.com           | Support@1    | SUPPORT  |
| Acme Corp   | support2@acme.com          | Support@2    | SUPPORT  |
| Beta Corp   | register.admin@beta.com    | Secret@1     | ADMIN    |
| Beta Corp   | support@beta.com           | Support@1    | SUPPORT  |
| Zeta Corp   | support@zeta.com           | Support@1    | SUPPORT  |


## Login with example user
```shell
# Authenticate and obtain access token
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{
"email": "admin@acme.com",
"password": "Secret@1"
}'

```
