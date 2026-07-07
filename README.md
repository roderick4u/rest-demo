# REST Demo

A Spring Boot REST API demo with two examples:

- A telecom-style notification gateway with API-key authentication, validation, rate limiting, retry behavior, notification history, and Swagger/OpenAPI.
- A simple cloud vendor CRUD API used as the original layered REST example.

## Tech Stack

- Java 25
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- MySQL for local runtime
- H2 in-memory database for tests
- Gradle
- JUnit 5 and Mockito
- Springdoc OpenAPI

## Project Structure

```text
src/main/java/com/restproject/rest_demo
├── carrier         # Simulated carrier delivery
├── client          # API clients and quota reset
├── controller      # REST endpoints
├── exception       # Custom exception and handler
├── model           # JPA entity
├── notification    # Notification API, DTOs, service, logs, exceptions
├── repository      # Spring Data JPA repository
├── response        # Response wrapper helper
└── service         # Service interface and implementation
```

Main flow:

```text
HTTP request -> Controller -> Service -> Repository -> Database
```

## Notification API

Base path:

```text
/api/v1/notifications
```

Authentication:

```text
X-API-Key: demo-api-key
```

The demo client is seeded from `demo.client.api-key` in `application.yaml`. The key is fixed for local demos and is not logged at startup.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/v1/notifications` | Send a notification |
| `GET` | `/api/v1/notifications/{id}` | Get one notification log |
| `GET` | `/api/v1/notifications/client/{clientId}?page=0&size=20` | Get paginated notification history for a client |

Send a notification:

```bash
curl -X POST http://localhost:8080/api/v1/notifications \
  -H "Content-Type: application/json" \
  -H "X-API-Key: demo-api-key" \
  -d '{
    "recipient": "+15551234567",
    "message": "Your verification code is 123456",
    "type": "CRITICAL"
  }'
```

Message types:

```text
CRITICAL, MARKETING, TRANSACTIONAL
```

Behavior:

- Invalid requests return `400 Bad Request`.
- Unknown API keys return `404 Not Found`.
- Rate-limited clients return `429 Too Many Requests`.
- Accepted requests consume quota before carrier delivery.
- `CRITICAL` messages retry up to 3 carrier attempts.
- `MARKETING` and `TRANSACTIONAL` messages attempt delivery once.
- Successful deliveries store the carrier reference in the notification log.
- Daily message counts reset at midnight with `@Scheduled(cron = "0 0 0 * * *")`.

## Swagger / OpenAPI

After starting the app, open:

```text
http://localhost:8080/swagger-ui.html
```

## Cloud Vendor API

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

- API-key authentication with `X-API-Key`
- Request validation with `@Valid`
- Rate limiting and scheduled daily quota reset
- Carrier retry rules by message type
- Notification logs with carrier references
- Paginated history endpoints with Spring Data `Pageable`
- Swagger/OpenAPI UI
- Basic CRUD REST endpoints
- Controller, service, and repository layering
- Spring Data JPA repository methods
- Custom exception handling for missing vendors
- Controller tests with `MockMvc`
- Service tests with Mockito
- Repository tests with `@DataJpaTest`
- H2 test database configuration

## Possible Improvements

- Use DTOs instead of exposing the JPA entity directly
- Return more precise HTTP status codes, such as `201 Created` for POST
- Make response bodies consistent across all endpoints
- Add a controller endpoint for searching by vendor name
- Move database credentials to environment variables
- Add Docker Compose for MySQL
- Add GitHub Actions for CI

## Status

This project is intended as a learning/demo REST API, not a production-ready service.
