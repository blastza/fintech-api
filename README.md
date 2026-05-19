# Fintech API

A backend REST API built with **Java 17** and **Spring Boot 4**, simulating core financial platform operations including user authentication, account management, and transaction processing.

---

## About the Project

This project was built to demonstrate real-world backend engineering practices in a fintech context. It follows RESTful API design principles and is structured for scalability, maintainability, and security — reflecting the kind of systems used in production financial environments.

**Currently in active development.** Planned enhancements include:
- Apache Kafka integration for event-driven, real-time transaction processing
- Frontend interface (React) for end-to-end demonstration

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT (jjwt 0.12.3) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Validation | Spring Boot Starter Validation |
| Build Tool | Maven |
| Utilities | Lombok |

---

## Features

- JWT-based authentication and authorisation
- RESTful API design with structured request/response models
- Input validation on all endpoints
- JPA-based data persistence with PostgreSQL
- Modular, layered architecture (Controller → Service → Repository)

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL running locally or via Docker

### Setup

```bash
# Clone the repository
git clone https://github.com/blastza/fintech-api.git
cd fintech-api
```

Configure your database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fintech_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The API will start on `http://localhost:8091`

---

## Project Structure

```
src/
└── main/
    └── java/
        └── com/platform-domain/fintech-api/
            ├── controller/    # REST endpoints
            ├── service/       # Business logic
            ├── repository/    # Data access layer
            ├── model/         # Entity classes
            ├── dto/           # Request/Response objects
            └── security/      # JWT & Spring Security config
```

---

## Roadmap

- [x] Spring Boot project setup
- [x] JWT authentication
- [x] Spring Security configuration
- [x] JPA + PostgreSQL integration
- [ ] Account management endpoints
- [ ] Transaction processing endpoints
- [ ] Apache Kafka event streaming
- [ ] Frontend (React)
- [ ] Unit & integration tests
- [ ] Docker Compose setup

---

## Author

**Lutendo Damuleli**
Java Backend Engineer | Spring Boot | Fintech

[LinkedIn](https://www.linkedin.com/in/lutendo-damuleli-123497282/) · [GitHub](https://github.com/blastza)
