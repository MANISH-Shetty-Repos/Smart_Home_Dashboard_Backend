# Smart Home Dashboard Backend

This project provides a robust and scalable backend infrastructure for a collaborative smart home management system. It enables secure user interaction with IoT devices, offering real-time status synchronization and a complete audit trail for environmental changes. The architecture emphasizes high data integrity, modular service layers, and production-ready containerization.

## Key Features

- User Authentication: Secure registration and login using JWT with access and refresh tokens.
- Room Management: Organize home layouts into rooms with nested device relationships.
- Device Control: Manage smart devices with dedicated toggle functionality for state changes.
- Audit History Tracking: Automated logging of device actions, user involvement, and timestamps.
- Real-Time Updates: WebSocket integration via STOMP for instant cross-client synchronization.
- API Documentation: Interactive Swagger UI support with integrated JWT authorization.

## Tech Stack

- Core Framework: Java 17 and Spring Boot 3.2.4
- Security: Spring Security 6 with BCrypt password hashing and JJWT library
- Persistence: MySQL 8.0, Spring Data JPA, and Hibernate ORM
- Database Governance: Flyway for automated schema migrations
- API Specification: OpenAPI 3 (Swagger UI) with JWT Bearer support
- Real-Time Communication: Spring WebSocket with STOMP message broker
- Infrastructure: Docker and Docker Compose for containerized environment management
- Utilities: Lombok, ModelMapper, and Slf4j logging
