# REST Demo

A Spring Boot REST API demo for managing cloud vendor records. The project shows a typical layered backend structure with a controller, service, repository, JPA entity, exception handling, and tests.

## Tech Stack

- Java 25
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- MySQL for local runtime
- H2 in-memory database for tests
- Gradle
- JUnit 5 and Mockito

## Project Structure

```text
src/main/java/com/restproject/rest_demo
├── controller      # REST endpoints
├── exception       # Custom exception and handler
├── model           # JPA entity
├── repository      # Spring Data JPA repository
├── response        # Response wrapper helper
└── service         # Service interface and implementation
```

Main flow:

```text
HTTP request -> Controller -> Service -> Repository -> Database
```

## API Endpoints

Base path:

```text
/cloudvendor
```

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/cloudvendor/{vendorId}` | Get one cloud vendor by ID |
| `GET` | `/cloudvendor` | Get all cloud vendors |
| `POST` | `/cloudvendor` | Create a cloud vendor |
| `PUT` | `/cloudvendor` | Update a cloud vendor |
| `DELETE` | `/cloudvendor/{vendorId}` | Delete a cloud vendor by ID |

## Example Request Body

```json
{
  "vendorId": "1",
  "vendorName": "Amazon",
  "vendorAdress": "USA",
  "vendorPhoneNumber": 1234
}
```

Note: `vendorAdress` is intentionally spelled this way because that is the current field name in the demo entity.

## Example curl Commands

Create a vendor:

```bash
curl -X POST http://localhost:8080/cloudvendor \
  -H "Content-Type: application/json" \
  -d '{
    "vendorId": "1",
    "vendorName": "Amazon",
    "vendorAdress": "USA",
    "vendorPhoneNumber": 1234
  }'
```

Get one vendor:

```bash
curl http://localhost:8080/cloudvendor/1
```

Get all vendors:

```bash
curl http://localhost:8080/cloudvendor
```

Update a vendor:

```bash
curl -X PUT http://localhost:8080/cloudvendor \
  -H "Content-Type: application/json" \
  -d '{
    "vendorId": "1",
    "vendorName": "Amazon Web Services",
    "vendorAdress": "USA",
    "vendorPhoneNumber": 5678
  }'
```

Delete a vendor:

```bash
curl -X DELETE http://localhost:8080/cloudvendor/1
```

## Prerequisites

- Java 25 JDK
- MySQL running locally
- A MySQL database named `rest_demo_db`
- A MySQL user matching the local configuration in `src/main/resources/application.yaml`

Current local database configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rest_demo_db
    username: rest_user
```

For your own machine, update `src/main/resources/application.yaml` with your MySQL username and password.

## Database Setup

Create the database:

```sql
CREATE DATABASE rest_demo_db;
```

Example user setup:

```sql
CREATE USER 'rest_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON rest_demo_db.* TO 'rest_user'@'localhost';
FLUSH PRIVILEGES;
```

Then update the password in:

```text
src/main/resources/application.yaml
```

Hibernate is currently configured with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
```

That means the schema is recreated when the application starts. This is acceptable for a demo, but not recommended for production.

## Run the Application

From the project root:

```bash
./gradlew bootRun
```

The API will start at:

```text
http://localhost:8080
```

## Run Tests

```bash
./gradlew test
```

Tests use H2 instead of MySQL, so the test suite does not require a running MySQL database.

The test database configuration is in:

```text
src/test/resources/application.yaml
```

## What This Demo Covers

- Basic CRUD REST endpoints
- Controller, service, and repository layering
- Spring Data JPA repository methods
- Custom exception handling for missing vendors
- Controller tests with `MockMvc`
- Service tests with Mockito
- Repository tests with `@DataJpaTest`
- H2 test database configuration

## Possible Improvements

- Add request validation with `@Valid`, `@NotBlank`, and related annotations
- Use DTOs instead of exposing the JPA entity directly
- Return more precise HTTP status codes, such as `201 Created` for POST
- Make response bodies consistent across all endpoints
- Add a controller endpoint for searching by vendor name
- Move database credentials to environment variables
- Add Swagger/OpenAPI documentation
- Add Docker Compose for MySQL
- Add GitHub Actions for CI

## Status

This project is intended as a learning/demo REST API, not a production-ready service.
