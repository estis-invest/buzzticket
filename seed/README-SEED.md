# Seed Data

## Run seeds files

```shell
cat ./seed/to-file.sql | docker exec -i buzzticket-db psql -U BUZZTICKET -d buzzticket  
```

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
