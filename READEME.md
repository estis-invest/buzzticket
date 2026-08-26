# WELCOME TO BUZZTICKET

This project is a self-hosted **multi-module** ticket management system and was intended as 
my first attempt at advanced software architecture focused on *Domain Driven Design* (*DDD*), *Hexagonal Architecture* and 
*Onion Architecture*.


## 📋 Requirements & Tech Stack Summary

### Runtime & Environment
* **Java 25:** Requires Java 25 SDK (utilizing dynamic agent loading and modern JVM features).
* **Docker Desktop / Engine:** Required locally to support containerized integration testing via Testcontainers.
* **Apache Maven 3.9+:** Multi-module build management.
* **PostgreSQL:** Relational database storage managed via automated Flyway schema migrations.

### Version Control & Quality Workflow
* **lazygit:** Terminal UI for Git version control management.
* **CodeRabbit AI:** AI-driven automated pull request code reviewer.

### Frameworks & Core Libraries
* **Spring Boot 4.0:** Base framework providing web REST APIs, Spring Security (OAuth2 / JWT Resource Server), and Spring Data JPA.
* **Nimbus JOSE JWT:** Cryptographic handling for RSA-signed token generation and verification.
* **Flyway:** Versioned database migration management.

### Testing Tools & Quality Assurance
* **JUnit 5:** Primary test framework for unit and integration testing.
* **Testcontainers:** Spins up real PostgreSQL instances inside temporary Docker containers for true-to-production database integration tests.
* **ArchUnit:** Enforces domain isolation and Hexagonal Architecture package boundary rules through automated unit tests.
* **AssertJ & Mockito:** Fluent assertions and behavior mocking for application/domain use-case tests.
* **Awaitility:** Asynchronous test verification utility.




