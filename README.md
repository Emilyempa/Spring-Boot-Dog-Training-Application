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

##  Authorization & Access Control

- **All endpoints require authentication.
- ** Unauthenticated users will only see the login page or an error response.
- **ADMIN** users can access all dogs and all training sessions.
- **USER** users can only access dogs and trainings that they own.
- Endpoints requiring elevated roles (USER or ADMIN):
    - `POST /api/dogs`
    - `POST /api/dogs/{dogId}/trainings`
    - `POST /api/dogtraining`
