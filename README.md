# Two Factor Demo

A small demo application for initializing and validating time-based one-time passwords (TOTP) with Spring Boot and Angular.

The project focuses on:

- generating a TOTP secret for a demo account
- returning an `otpauth://` URL that can be rendered as a QR code
- scanning the QR code with Google Authenticator or another TOTP app
- validating a six-digit TOTP code
- resetting the stored demo secret
- showing basic backend request validation and HTTP error handling

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- MySQL 8
- Angular 21
- Nginx
- Docker Compose

## Project Structure

```text
.
+-- src/main/java/...          Spring Boot backend
+-- src/main/resources         Backend configuration
+-- frontend                   Angular frontend
+-- docker/compose.yml         Docker Compose setup
+-- Dockerfile                 Backend Docker image
+-- pom.xml                    Maven build file
```

## Requirements

- Docker
- Docker Compose

For local development without Docker, you also need:

- Java 21
- Maven or the included Maven wrapper
- Node.js and npm
- MySQL 8

## Run With Docker

From the project root:

```bash
docker compose -f docker/compose.yml up --build
```

After the containers start:

- Frontend: <http://localhost>
- Backend: <http://localhost:8080>
- MySQL: `localhost:3306`

To stop the application:

```bash
docker compose -f docker/compose.yml down
```

## Demo Flow

1. Open <http://localhost>.
2. Click **Try two-factor**.
3. The backend creates or reuses a TOTP secret for the demo account.
4. The frontend renders the returned `otpauth://` URL as a QR code.
5. Scan the QR code with Google Authenticator.
6. Enter the six-digit code from the authenticator app.
7. Submit the code.
8. The backend validates the code against the current TOTP time window.

The demo uses a fixed backend account:

```text
test@test.com
```

## API Endpoints

Base URL:

```text
http://localhost:8080/api/v1/two-factor
```

### Generate TOTP URL

```http
POST /generate-url
```

Response:

```json
{
  "url": "otpauth://totp/two-factor-demo:test@test.com?secret=...&issuer=MilthDev"
}
```

### Validate TOTP Code

```http
POST /validate
Content-Type: application/json
```

Request:

```json
{
  "code": "123456"
}
```

The `code` value must be a six-digit string.

Successful response:

```http
204 No Content
```

Invalid or malformed requests return `400 Bad Request`.

### Reset Demo Secret

```http
POST /reset
```

Successful response:

```http
204 No Content
```

After reset, generate a new QR code before validating another TOTP code.

## Backend Configuration

The backend configuration is in `src/main/resources/application.yaml`.

Default datasource:

```yaml
spring:
  datasource:
    url: jdbc:mysql://host.docker.internal:3306/mydatabase
    username: root
    password: verysecret
```

The Docker Compose setup starts a MySQL database with the same database name and password.

## Local Development

Start MySQL first, then run the backend:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Run the frontend separately:

```bash
cd frontend
npm install
npm start
```

Angular usually serves the app at:

```text
http://localhost:4200
```

## Validation Notes

The validation endpoint expects this shape:

```json
{
  "code": "123456"
}
```

The code is intentionally represented as a string because TOTP codes may start with `0`.

## Security Notes

This is demo code, not a production-ready authentication system.

Current demo simplifications:

- the account email is hardcoded
- CORS is open
- the TOTP secret is stored as plain text
- there is no login/session model
- there is no rate limiting
- reset is intentionally simple

For production, add authenticated users, encrypted secret storage, rate limiting, stricter CORS, proper account ownership checks, and a confirmed two-factor enrollment state.
