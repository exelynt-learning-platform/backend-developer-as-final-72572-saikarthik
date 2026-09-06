# Resource Booking System - Spring Boot REST API

A comprehensive RESTful API for managing resource bookings (rooms, equipment, vehicles) with JWT-based authentication and role-based access control (ADMIN/USER roles).

## Features

- **User Authentication**: JWT-based stateless authentication with secure password encoding
- **Role-Based Access Control (RBAC)**: ADMIN and USER roles with method-level security
- **Resource Management**: Full CRUD operations for resources with ADMIN-only modifications
- **Reservation Management**: Users can book resources with automatic validation of time ranges and availability
- **Advanced Filtering**: Filter reservations by user, status, and price range with pagination support
- **Pagination & Sorting**: All list endpoints support pagination and custom sorting
- **Input Validation**: Comprehensive Jakarta validation with detailed error messages
- **Global Exception Handling**: Centralized error handling with proper HTTP status codes
- **API Documentation**: Swagger UI with OpenAPI 3.0 specification
- **Audit Tracking**: Automatic timestamp tracking for resource creation and updates
- **Database Support**: MySQL 8.0+ with Hibernate/JPA ORM

## Prerequisites

- **Java**: OpenJDK 17 or higher
- **Maven**: 3.8.0 or higher
- **MySQL**: 8.0 or higher
- **Git**: For version control

## Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd resource-booking-system
```

### 2. Create Database
```sql
CREATE DATABASE resource_booking_db;
USE resource_booking_db;
```

### 3. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/resource_booking_db
spring.datasource.username=root
spring.datasource.password=root
```

**Alternative: PostgreSQL Configuration**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/resource_booking_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/api`

## Authentication

### Login Endpoint
**POST** `/api/auth/login`

Request:
```json
{
	"username": "admin",
	"password": "admin123"
}
```

Response:
```json
{
	"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Using JWT Token
Include the token in the Authorization header for all authenticated requests:
```
Authorization: Bearer <your_jwt_token>
```

## Seed Users

The application comes pre-loaded with the following test users:

| Username | Password | Role  | Email               |
|----------|----------|-------|---------------------|
| admin    | admin123 | ADMIN | admin@example.com   |
| user1    | user123  | USER  | user1@example.com   |
| user2    | user123  | USER  | user2@example.com   |

**Note**: Passwords are BCrypt-encoded. Modify `data.sql` to add more users.

## API Endpoints

### Authentication
| Method | Endpoint       | Description           | Auth Required |
|--------|----------------|-----------------------|---------------|
| POST   | `/auth/login`  | User login            | No            |

### Resources
| Method | Endpoint         | Description              | Auth Required | Role Required |
|--------|------------------|--------------------------|---------------|---------------|
| GET    | `/resources`     | List all resources       | No            | -             |
| GET    | `/resources/{id}`| Get resource by ID       | No            | -             |
| POST   | `/resources`     | Create new resource      | Yes           | ADMIN         |
| PUT    | `/resources/{id}`| Update resource          | Yes           | ADMIN         |
| DELETE | `/resources/{id}`| Delete resource          | Yes           | ADMIN         |

### Reservations
| Method | Endpoint                     | Description                    | Auth Required | Role Required |
|--------|------------------------------|--------------------------------|---------------|---------------|
| GET    | `/reservations`              | List reservations (filtered)   | Yes           | ADMIN/USER    |
| GET    | `/reservations/{id}`         | Get reservation by ID          | Yes           | ADMIN/USER    |
| POST   | `/reservations`              | Create new reservation         | Yes           | ADMIN/USER    |
| PUT    | `/reservations/{id}`         | Update reservation             | Yes           | ADMIN/USER    |
| DELETE | `/reservations/{id}`         | Delete reservation             | Yes           | ADMIN/USER    |
| PATCH  | `/reservations/{id}/status`  | Update status (ADMIN only)     | Yes           | ADMIN         |

## API Examples

### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
	-H "Content-Type: application/json" \
	-d '{
		"username": "admin",
		"password": "admin123"
	}'
```

### 2. Get All Resources
```bash
curl http://localhost:8080/api/resources?page=0&size=10
```

### 3. Create Resource (ADMIN only)
```bash
curl -X POST http://localhost:8080/api/resources \
	-H "Authorization: Bearer <token>" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Laptop",
		"description": "Dell XPS 13",
		"type": "Equipment",
		"price": 30.00,
		"available": true
	}'
```

### 4. Create Reservation
```bash
curl -X POST http://localhost:8080/api/reservations \
	-H "Authorization: Bearer <token>" \
	-H "Content-Type: application/json" \
	-d '{
		"resourceId": 1,
		"startTime": "2026-09-25T10:00:00",
		"endTime": "2026-09-25T12:00:00",
		"price": null
	}'
```

### 5. Get Filtered Reservations
```bash
curl "http://localhost:8080/api/reservations?status=CONFIRMED&minPrice=0&maxPrice=100&page=0&size=10&sortBy=createdAt&sortDirection=desc" \
	-H "Authorization: Bearer <token>"
```

### 6. Update Reservation Status (ADMIN only)
```bash
curl -X PATCH "http://localhost:8080/api/reservations/1/status?status=CONFIRMED" \
	-H "Authorization: Bearer <token>"
```

## Swagger Documentation

Access the interactive Swagger UI at:
```
http://localhost:8080/api/swagger-ui.html
```

Or access the raw OpenAPI 3.0 specification:
```
http://localhost:8080/api/v3/api-docs
```

## Technology Stack

- **Spring Boot**: 4.1.1 (Spring Framework 6.x)
- **Spring Security**: 6.x with JWT authentication
- **Spring Data JPA**: ORM and data access
- **Spring Doc OpenAPI**: Swagger UI documentation
- **Hibernate**: 6.x ORM framework
- **MySQL Connector**: 8.x driver
- **JJWT**: 0.12.6 (JWT library)
- **Lombok**: Boilerplate reduction
- **Jakarta Validation**: Input validation
- **Maven**: Build automation
- **Java**: Version 17+

## Database Schema

### Users Table
```sql
CREATE TABLE users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	INDEX idx_username (username)
);
```

### Resources Table
```sql
CREATE TABLE resource (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	type VARCHAR(50) NOT NULL,
	price DECIMAL(10,2) NOT NULL,
	available BOOLEAN DEFAULT true,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Reservations Table
```sql
CREATE TABLE reservation (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	resource_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	start_time DATETIME NOT NULL,
	end_time DATETIME NOT NULL,
	price DECIMAL(10,2),
	status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') DEFAULT 'PENDING',
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (resource_id) REFERENCES resource(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	INDEX idx_user_id (user_id),
	INDEX idx_resource_id (resource_id),
	INDEX idx_status (status)
);
```

## Troubleshooting

### Issue: Database Connection Failed
**Solution**: Verify MySQL is running and credentials are correct in `application.properties`
```bash
# Linux/Mac
sudo systemctl start mysql

# Windows
net start MySQL80
```

### Issue: JWT Token Expired
**Solution**: Token expires in 24 hours. Log in again to get a new token
```bash
# Check token expiration in application.properties
jwt.expiration.time=86400000
```

### Issue: CORS Errors
**Solution**: If frontend is on different origin, update SecurityConfig:
```java
.requestMatchers("/**").permitAll()  // Allow all origins
```

### Issue: Port 8080 Already in Use
**Solution**: Change port in `application.properties`:
```properties
server.port=8081
```

## Project Structure

```
src/main/java/com/example/resource_booking_system/
├── config/
│   ├── SecurityConfig.java
│   ├── OpenApiConfig.java
│   └── UserPrincipal.java
├── controller/
│   ├── AuthController.java
│   ├── ResourceController.java
│   └── ReservationController.java
├── service/
│   ├── AuthService.java
│   ├── ResourceService.java
│   ├── ReservationService.java
│   └── CustomUserDetailsService.java
├── repository/
│   ├── UserRepository.java
│   ├── ResourceRepository.java
│   └── ReservationRepository.java
├── entity/
│   ├── User.java
│   ├── Resource.java
│   └── Reservation.java
├── dto/
│   ├── login/
│   │   ├── LoginRequest.java
│   │   └── LoginResponse.java
│   ├── resources/
│   │   ├── ResourceRequest.java
│   │   └── ResourceResponse.java
│   └── reservation/
│       ├── ReservationRequest.java
│       └── ReservationResponse.java
├── enums/
│   ├── ReservationStatus.java
│   └── Role.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── Error.java
└── ResourceBookingSystemApplication.java
```

## Security Considerations

- All passwords are BCrypt-encoded with strength 12
- JWT tokens expire in 24 hours
- Sensitive endpoints require ADMIN role authorization
- Database connections use prepared statements to prevent SQL injection
- CSRF protection disabled for REST API (stateless JWT auth)
- Session creation disabled (STATELESS policy)

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthServiceTest

# Run with coverage
mvn test jacoco:report
```

## Deployment

### Build JAR
```bash
mvn clean package
```

### Run JAR
```bash
java -jar target/resource-booking-system-0.0.1-SNAPSHOT.jar
```

### Docker Deployment
Create `Dockerfile`:
```dockerfile
FROM eclipse-temurin:17-jdk
COPY target/resource-booking-system-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t resource-booking-system .
docker run -p 8080:8080 resource-booking-system
```

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add new feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues or questions, please open an issue on GitHub or contact the development team.

---

**Last Updated**: September 2026
**Version**: 1.0.0
**Status**: Production Ready
