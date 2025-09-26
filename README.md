# 📺 Fynema API

The **Fynema API** is the core application that enables searching, downloading, and managing media through services like  
**Jackett**, **Jellyfin**, or **Plex**.  
It is built with **Spring Boot** and exposes **REST** endpoints secured with **JWT**, as well as a **WebSocket** channel  
to track download progress in real time.

---

## 🚀 Getting Started

### 🔧 Prerequisites

- **Java 25**
- **Maven 3.9+**
- A **MariaDB** or **MySQL** instance
- A **Jackett** instance configured with your indexers
- (Optional) A **Jellyfin** or **Plex** instance for automatic library refresh

### ▶️ Run locally

Clone the project, then run:

```bash
mvn spring-boot:run
```

By default, the API runs at **[http://localhost:8080](http://localhost:8080)**.

---

## ⚙️ Configuration

### `application.properties` (example)

```properties
# Database
spring.datasource.url=jdbc:mariadb://localhost:3306/fynema
spring.datasource.username=fynema
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
security.jwt.accessSecret=super_secret_key_change_me
security.jwt.refreshSecret=another_super_secret_key_change_me_too
# 15 minutes
security.jwt.expiration=900000
```

---

## 👤 Default admin user

On the very first startup, if the database is empty, a default **`admin`** user is automatically created.  
A secure random password (16 characters) is generated and printed in the logs at startup:

```
Default admin user created with username 'admin'.
Please change the password after first login,

the password is: <generated_password>
```

⚠️ **Important**: Make sure to change this password immediately after the first login to secure your instance.

---

## 🔑 Security (JWT)

All protected endpoints require a **JWT token**.

### Authentication flow

1. **Login**:  
   `POST /auth/login`  
   Payload:

   ```json
   {
     "username": "admin",
     "password": "mypassword"
   }
   ```

   Response:

   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9..."
   }
   ```

2. **Access protected routes**:  
   Add this header to every request:

   ```
   Authorization: Bearer <token>
   ```

---

## 🛠️ Main technologies

* **Spring Boot 3.5.6**
* **Spring Data JPA** → persistence
* **Spring Security + JWT (jjwt 0.13.0)** → security
* **Spring WebSocket (STOMP)** → real-time notifications
* **MariaDB Driver** → database connection
* **BouncyCastle + Argon2** → password hashing
* **Lombok** → boilerplate reduction
* **Actuator** → monitoring & technical endpoints
* **Maven** → build & dependency management

---

## 📡 Available endpoints

* `POST /auth/login` → authenticate and generate a JWT
* `POST /auth/register` → register a new user
* `GET /users` → list users (JWT required)

---

## 📜 License

⚠️ **Strictly personal use only**.  
This code is not intended for public distribution or commercial use.
