# HDFC Life Policy Desk API

A Spring Boot 3 + Java 17 REST API for managing HDFC Life policies and claims. Policies and claims are stored in an **in-memory store**, while **Flyway owns the PostgreSQL-ready schema** for future persistence.  

Profiles: **dev (H2)** and **prod (PostgreSQL)**.  

API documentation provided via **Springdoc OpenAPI**.

---

## Features

- Spring Boot 3 (web, validation, data-jpa for datasource only)
- In-memory policy and claim storage (no JPA entities)
- Flyway-managed schema (PostgreSQL-ready)
- Dev profile uses H2 with PostgreSQL mode
- Constructor injection only (no field autowiring)
- CommandLineRunner data seeding (6 policies)
- Swagger UI with full OpenAPI annotations
- Global exception handling via `@RestControllerAdvice`
- Proper HTTP semantics (201 create, 204 delete, 404 not found, 409 conflict)
- Strict JSON field names (`policyNo`, `customer`, `type`, `basePremium`, `status`, `claimNo`, etc.)

---

## How to Run

### **Dev Profile (default, H2)**  
```
mvn spring-boot:run
```

### **Prod Profile (PostgreSQL)**
Set environment variables:


```
export DB_URL=jdbc:postgresql://localhost:5432/hdfc 
export DB_USER=postgres export DB_PASSWORD=secret
```

Run with profile:

```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Swagger / OpenAPI

### Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON:
```
http://localhost:8080/v3/api-docs
```
---
## API EndPoints 

| Method     | Path                              | Description / Behaviour                                                                                                       | Success Codes      | Error Codes                                                                               |
| ---------- | --------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- | ------------------ | ----------------------------------------------------------------------------------------- |
| **GET**    | `/api/policies`                   | Return **all policies** in insertion order. Supports optional filters: `status`, `type`, and combined filter `status + type`. | **200 OK**         | —                                                                                         |
| **GET**    | `/api/policies/{policyNo}`        | Return one policy by policy number.                                                                                           | **200 OK**         | **404 Not Found** (PolicyNotFoundException)                                               |
| **GET**    | `/api/policies?status=Active`     | Filter policies by **status** (exact match).                                                                                  | **200 OK**         | —                                                                                         |
| **GET**    | `/api/policies?type=TERM`         | Filter policies by **type** (exact match).                                                                                    | **200 OK**         | —                                                                                         |
| **POST**   | `/api/policies`                   | Create a new policy. Adds insertion order. Returns `Location: /api/policies/{policyNo}`.                                      | **201 Created**    | **409 Conflict** (DuplicatePolicyException)                                               |
| **PUT**    | `/api/policies/{policyNo}`        | Replace **customer**, **type**, **basePremium**, **status**. Body policyNo ignored; path variable wins.                       | **200 OK**         | **404 Not Found** (PolicyNotFoundException)                                               |
| **DELETE** | `/api/policies/{policyNo}`        | Delete a policy. Removes it from insertion order.                                                                             | **204 No Content** | **404 Not Found** (PolicyNotFoundException)                                               |
| **GET**    | `/api/policies/{policyNo}/claims` | Get all claims for a policy (oldest first).                                                                                   | **200 OK**         | **404 Not Found** (PolicyNotFoundException)                                               |
| **POST**   | `/api/claims`                     | File a claim. Body: `policyNo`, `claimAmount`, `urgency`. Auto-assigns claimNo (`CLM-01`, `CLM-02`, …).                       | **201 Created**    | **400 Bad Request** (InvalidClaimException) / **404 Not Found** (PolicyNotFoundException) |
| **GET**    | `/api/claims/{claimNo}`           | Get a specific claim by claimNo.                                                                                              | **200 OK**         | **404 Not Found** (ClaimNotFoundException)                                                |

---
## Entity - Relationship List

**customers**: Holds unique customers. Policies reference customers through customer_id.


| Column         | Rules                 |
| -------------- | --------------------- |
| **id**         | Primary key, identity |
| **full\_name** | NOT NULL, UNIQUE      |
| **email**      | NOT NULL, UNIQUE      |


**policies**: Policy → Customer = Many-to-One



| Column            | Rules                                              |
| ----------------- | -------------------------------------------------- |
| **id**            | Primary key, identity                              |
| **policy\_no**    | NOT NULL, UNIQUE                                   |
| **customer\_id**  | NOT NULL, foreign key → **customers(id)**          |
| **product\_type** | NOT NULL, CHECK in (`TERM`, `ULIP`, `ENDOWMENT`)   |
| **base\_premium** | NOT NULL, CHECK > 0                                |
| **status**        | NOT NULL, CHECK in (`Active`, `Lapsed`, `Pending`) |


**claims**: Claim → Policy = Many-to-One



| Column         | Rules                                                    |
| -------------- | -------------------------------------------------------- |
| **id**         | Primary key, identity                                    |
| **claim\_no**  | NOT NULL, UNIQUE                                         |
| **policy\_id** | NOT NULL, foreign key → **policies(id)**                 |
| **amount**     | NOT NULL, CHECK > 0                                      |
| **urgency**    | NOT NULL, CHECK in (`HIGH`, `MEDIUM`, `LOW`)             |
| **status**     | NOT NULL, CHECK in (`SUBMITTED`, `APPROVED`, `REJECTED`) |


**riders**: Reference table for insurance rider types.



   | Column   | Rules                 |
   | -------- | --------------------- |
   | **id**   | Primary key, identity |
   | **code** | NOT NULL, UNIQUE      |
   | **name** | NOT NULL              |
   

**policy_riders** (Junction Table): Policy ↔ Rider = Many-to-Many



| Column          | Rules                               |
| --------------- | ----------------------------------- |
| **policy\_id**  | Foreign key → **policies(id)**      |
| **rider\_id**   | Foreign key → **riders(id)**        |
| **PRIMARY KEY** | Composite (`policy_id`, `rider_id`) |



---
## Choice of In-Memory and Postgres Databases
We use in-memory when we need ultra-fast access, simple demos, local development, or workloads where data does not need to survive restarts. It’s ideal for lightweight, single‑instance tools.
We move to PostgreSQL when durability, multi-instance consistency, reporting, backups, and relational integrity become important.

Flyway gives you controlled, versioned, repeatable schema migrations that are predictable and CI/CD-friendly.
In contrast, ddl-auto=update applies implicit, environment-dependent schema changes at runtime, making it unsuitable for production.
