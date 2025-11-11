# Dog Training Application

## Description
This is a RESTful dog training application that allows users to create and manage their dogs and their training sessions.

**Tech stack:**
- Spring Boot 3.5+
- Java 25
- Spring Data JPA (MySQL 9+)
- Spring Security
- Docker Compose
- Flyway for migrations



### Root URL: localhost:8080

## DogController Endpoints (`/api/dogs`)

| Method | Endpoint                          | Description |
|--------|-----------------------------------|-------------|
| GET    | `/api/dogs`                       | Get all dogs accessible to the authenticated user |
| GET    | `/api/dogs/{id}`                  | Get a specific dog by ID |
| POST   | `/api/dogs`                       | Create a new dog (requires USER or ADMIN role) |
| PUT    | `/api/dogs/{id}`                  | Update an existing dog by ID |
| DELETE | `/api/dogs/{id}`                  | Delete a dog by ID |
| GET    | `/api/dogs/{dogId}/trainings`     | Get all trainings for a specific dog, optionally filter by activity |
| POST   | `/api/dogs/{dogId}/trainings`     | Add a new training for a specific dog (requires USER or ADMIN role) |

---

## DogTrainingController Endpoints (`/api/dogtraining`)

| Method | Endpoint                          | Description |
|--------|-----------------------------------|-------------|
| GET    | `/api/dogtraining`                | Get all dog training sessions |
| GET    | `/api/dogtraining/{id}`           | Get a specific training session by ID |
| POST   | `/api/dogtraining`                | Create a new training session (requires USER or ADMIN role) |
| DELETE | `/api/dogtraining/{id}`           | Delete a training session by ID |

---

## Authorization & Access Control

- All endpoints require authentication.
- Unauthenticated users will only see the login page or an error response.
- **ADMIN** users can access all dogs and all training sessions.
- **USER** users can only access dogs and trainings that they own.
- Endpoints requiring elevated roles (USER or ADMIN):
    - `POST /api/dogs`
    - `POST /api/dogs/{dogId}/trainings`
    - `POST /api/dogtraining`

## TESTS
Testing of:
REST controller endpoints
Security access control (authenticated vs unauthenticated)

Run test with `mvn test`


## Setup

- Prerequisites:
  - Java 25 (JDK) installed and on PATH
  - Maven 3.9+ (comes with many IDEs, or install separately)
  - Docker Desktop with Docker Compose v2
  - Git

- Quick start (recommended):
  1) Start the MySQL database with Docker Compose
     - Command: `docker compose up -d`
     - This will start a MySQL 9+ container with database dog_training_db
  2) Run the application
     - Command: `mvn spring-boot:run`
     - App starts on `http://localhost:8080`
  3) Verify the app is up
     - Open `http://localhost:8080` or call an API endpoint (requires login)

- Alternative (use your own MySQL):
  - Ensure a MySQL server is running and reachable on `localhost:3306` (or update the URL)
  - Create database/schema: `dog_training_db`
  - Set credentials to match your server in `src/main/resources/application.properties` or via environment variables
  - On app start, Flyway will automatically create/update tables

- Notes:
  - Default DB credentials are configured in application.properties
  - Flyway automatically creates and updates tables on startup.
 
- Build a runnable JAR:
  - `mvn clean package`
  - Run: `java -jar target/exercise2025-*.jar`

## How the database is managed

- The project uses Flyway migrations under `resources/db/migration`
- Migrations are applied automatically on startup (`spring.flyway.baseline-on-migrate=true`)