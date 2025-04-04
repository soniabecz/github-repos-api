# github-repos-api

## Description

This is a Spring Boot application written in Java 21. It exposes a REST API that retrieves **non-fork GitHub repositories** for a given user and lists the **branches** along with their **last commit SHA**.

---

## Technologies Used

- Java 21
- Spring Boot
- Maven
- RestTemplate
- GitHub REST API v3
- JUnit 5
- Spring MockMvc (integration testing)

---

## Running the Application

```bash
mvn spring-boot:run
```

The server runs by default on [http://localhost:8080](http://localhost:8080)

---

## 📂 Endpoint

### `GET /repos/{username}`

Returns a list of **non-forked public repositories** of the specified GitHub user, including all branches and their latest commit SHA.

#### ✅ Sample Success Response:
```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123def456"
      }
    ]
  }
]
```

#### ❌ Sample Error (User Not Found):
```json
{
  "status": 404,
  "message": "User not found"
}
```

---

## 🧪 Testing

To execute integration tests:

```bash
mvn test
```

Covered scenarios include:

- Successful repository fetch
- Handling of non-existing GitHub user (404)
- Handling users with no repositories
- Ensuring only non-forked repos are returned
- Ensuring each branch has a name and commit SHA

---

## 📌 Notes

- No pagination support (as per requirements)
- No authentication required
- No usage of WebFlux
- Fully synchronous HTTP via `RestTemplate`

---

## ✍️ Author

Sonia Becz

