# 📺 Fynema API

L’API **Fynema** est le cœur applicatif permettant de rechercher, télécharger et gérer des médias via des services comme **Jackett**, **Jellyfin** ou **Plex**.  
Elle est développée en **Spring Boot** et expose des endpoints **REST** sécurisés avec **JWT**, ainsi qu’un canal **WebSocket** pour suivre l’avancement des téléchargements en temps réel.

---

## 🚀 Démarrage

### 🔧 Prérequis
- **Java 25**
- **Maven 3.9+**
- Une instance de **MariaDB** ou **MySQL**
- Une instance de **Jackett** configurée avec vos indexers
- (Optionnel) Une instance **Jellyfin** ou **Plex** pour l’actualisation automatique des bibliothèques

### ▶️ Lancer en local
Clonez le projet, puis exécutez :

```bash
mvn spring-boot:run
````

Par défaut, l’API écoute sur **[http://localhost:8080](http://localhost:8080)**.

---

## ⚙️ Configuration

### `application.properties` (exemple)

```properties
# Base de données
spring.datasource.url=jdbc:mariadb://localhost:3306/fynema
spring.datasource.username=fynema
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
security.jwt.secret=super_secret_key_change_me
security.jwt.expiration=86400000 # 1 jour en ms
```

---

## 🔑 Sécurité (JWT)

Tous les endpoints protégés nécessitent un **token JWT**.

### Flux d’authentification

1. **Connexion** :
   `POST /auth/login`
   Payload :

   ```json
   {
     "username": "test",
     "password": "secret"
   }
   ```

   Réponse :

   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9..."
   }
   ```

2. **Requêtes protégées** :
   Ajoutez ce header à chaque requête :

   ```
   Authorization: Bearer <token>
   ```

---

## 🛠️ Technologies principales

* **Spring Boot 3.5.6**
* **Spring Data JPA** → persistance
* **Spring Security + JWT (jjwt 0.13.0)** → sécurité
* **Spring Websocket (STOMP)** → notifications en temps réel
* **MariaDB Driver** → base de données
* **BouncyCastle + Argon2** → hashage des mots de passe
* **Lombok** → réduction du boilerplate
* **Actuator** → monitoring et endpoints techniques
* **Maven** → build & gestion des dépendances

---

## 📡 Endpoints disponibles

* `POST /auth/login` → authentification, génère un JWT
* `POST /auth/register` → inscription d’un nouvel utilisateur
* `GET /users` → liste les utilisateurs (protégé par JWT)

---

## 📜 Licence

⚠️ **Usage strictement personnel**.
Le code n’est pas destiné à une distribution publique ni à un usage commercial.

```