---
---

### 📁 `README.md`

````markdown
# 📝 Task API

A production-grade **Spring Boot** REST API for managing Todos, powered by **PostgreSQL**, tested with **JUnit 5**, **Cucumber**, and **Testcontainers**. Built with GitHub Actions for CI.

---

## 🚀 Features

- ✅ Create, retrieve, and delete todo items
- 🔐 Stateless RESTful API design
- 🧪 BDD testing with Cucumber + Gherkin
- 🔄 Integration testing with Testcontainers (PostgreSQL)
- ☁️ GitHub Actions CI pipeline

---

## 📦 Tech Stack

| Layer         | Tech                                |
|---------------|-------------------------------------|
| Framework     | Spring Boot 3.x                     |
| Database      | PostgreSQL 15 (via Testcontainers)  |
| ORM           | Spring Data JPA                     |
| Testing       | JUnit 5, Cucumber, Testcontainers   |
| Build Tool    | Gradle                              |
| CI/CD         | GitHub Actions                      |

---

## ⚙️ Getting Started

### 🚧 Prerequisites

- Java 21+
- Gradle 8+
- Docker (for Testcontainers)

### 🛠️ Run locally

```bash
./gradlew bootRun
````

The API will be available at:
📍 `http://localhost:8080/api/v1/todos`

---

## 🧪 Running Tests

### ✅ Unit & Integration Tests

```bash
./gradlew test
```

Runs:

* JUnit unit tests
* Testcontainers-backed integration tests
* Cucumber feature tests

---

### 🥒 Cucumber Gherkin Scenarios

Located in:
📄 `src/test/resources/features/todo.feature`

Example:

```gherkin
Feature: Todo API

  Scenario: Create a new todo
    Given I have a todo with title "Test Task" and completed "false"
    When I create the todo
    Then the todo should be saved successfully
```

Step definitions in:
📄 `TodoSteps.java`

---

## 🐳 Testcontainers Setup

PostgreSQL is run in a container automatically via:

📄 `AbstractPostgresContainerTest.java`

```java
@Container
static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
```

Properties overridden via `@DynamicPropertySource`.

---

## 🤖 GitHub Actions CI

CI pipeline runs on every push to `main` or `develop`:

📁 `.github/workflows/test.yml`

* Runs Gradle tests
* Spins up PostgreSQL service
* Caches dependencies
* Publishes test reports

---

## 🧾 API Endpoints

| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| POST   | `/api/v1/todos`      | Create a new todo |
| GET    | `/api/v1/todos`      | List all todos    |
| GET    | `/api/v1/todos/{id}` | Get todo by ID    |
| DELETE | `/api/v1/todos/{id}` | Delete todo by ID |

---

## 🧹 Project Structure

```
src/
├── main/
│   ├── java/com/gmdt/task/
│   │   ├── controller/       # REST controllers
│   │   ├── service/          # Business logic
│   │   ├── model/            # Entity classes
│   │   └── repository/       # JPA Repositories
│
├── test/
│   ├── java/com/gmdt/task/
│   │   ├── steps/            # Cucumber step defs
│   │   ├── controller/       # Integration tests
│   │   └── CucumberRunnerTest.java
│
│   └── resources/features/   # .feature files
```

---

## 🧑‍💻 Author

**GMDT Dev Team**
Built with ❤️ for clean code and solid tests.

---

## 📄 License

MIT © GMDT. See [LICENSE](./LICENSE) for details.
```
