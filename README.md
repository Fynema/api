# Fynema API

L’API **Fynema** est le cœur applicatif permettant de rechercher, télécharger et gérer des médias via des services comme **Jackett**, **Jellyfin** ou **Plex**.  
Elle est développée en **Spring Boot** et expose des endpoints REST sécurisés avec **JWT**, ainsi qu’un canal **WebSocket** pour suivre l’avancement des téléchargements en temps réel.

---

## 🚀 Démarrage

### Prérequis
- **Java 25**
- **Maven 3.9+**
- Une instance de **Jackett** configurée avec vos indexers.
- (Optionnel) Une instance **Jellyfin** ou **Plex** pour l’actualisation automatique des bibliothèques.

### Lancer en local
```bash
mvn spring-boot:run
````

Par défaut, l’API écoute sur **[http://localhost:8080](http://localhost:8080)**

---

## 🔑 Sécurité (JWT)

Tous les endpoints sont sécurisés par un **token JWT**.
Flux d’authentification typique :

1. `POST /auth/login` → renvoie un token JWT.
2. Chaque appel doit inclure :

   ```
   Authorization: Bearer <token>
   ```

---

## 🛠️ Technologies principales

* **Spring Boot 3.5.6**
* **Spring Data JPA** : persistance
* **Spring Security + JWT** : sécurité
* **Spring Websocket (STOMP)** : notifications temps réel
* **Lombok** : réduction du boilerplate
* **Maven** : build & gestion des dépendances
