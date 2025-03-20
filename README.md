# Message Server Application

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── se/
│   │       └── gritacademy/
│   │           ├── MessageServerApplication.java
│   │           ├── api/
│   │           │   ├── AdminController.java
│   │           │   ├── MessageController.java
│   │           │   └── UserController.java
│   │           ├── controller/
│   │           │   └── AuthController.java
│   │           ├── model/
│   │           │   ├── Message.java
│   │           │   └── UserInfo.java
│   │           ├── repository/
│   │           │   ├── MessageRepository.java
│   │           │   └── UserRepository.java
│   │           ├── service/
│   │           │   ├── AuthService.java
│   │           │   ├── MessageService.java
│   │           │   └── UserService.java
│   │           └── util/
│   │               ├── JwtUtil.java
│   │               └── LoggerUtil.java
│   ├── resources/
│   │   ├── application.properties
│   │   └── static/
│   │       ├── admin.html
│   │       ├── login.html
│   │       ├── register.html
│   │       └── user.html
└── test/
    └── java/
```

## Main Classes and Responsibilities

### Controllers
- **AuthController**: Handles authentication endpoints (login, register, logout)
- **MessageController**: Manages message-related operations (send, retrieve messages)
- **UserController**: Handles user management operations
- **AdminController**: Provides admin-specific functionality

### Models
- **Message**: Represents a message entity with sender, recipient, and content
- **UserInfo**: Represents user information including email, password hash, and role

### Services
- **AuthService**: Handles authentication logic
- **MessageService**: Manages message-related business logic
- **UserService**: Provides user management functionality

### Repositories
- **MessageRepository**: Data access layer for messages
- **UserRepository**: Data access layer for users

### Utilities
- **JwtUtil**: Handles JWT token generation and validation
- **LoggerUtil**: Provides logging functionality

## API Endpoints

### Authentication (/api)
- POST /register - Register new user
  ```bash
  curl -X POST http://localhost:8080/api/register \
    -H "Content-Type: application/json" \
    -d '{"email":"user@example.com","password":"securePassword123"}'
  ```

- POST /login - User login
  ```bash
  curl -X POST http://localhost:8080/api/login \
    -H "Content-Type: application/json" \
    -d '{"email":"user@example.com","password":"securePassword123"}'
  ```

- GET /logout - User logout
  ```bash
  curl -X GET http://localhost:8080/api/logout \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```

- GET /users - List all users (requires authentication)
  ```bash
  curl -X GET http://localhost:8080/api/users \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```
  Returns:
  ```json
  ["user1@example.com", "user2@example.com"]
  ```

### Messages (/api/messages)
- GET / - Get messages for authenticated user
  ```bash
  curl -X GET http://localhost:8080/api/messages \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```
  Returns:
  ```json
  [
    {
      "date": "2025-03-20T02:45:00Z",
      "sender": "user1@example.com",
      "message": "Hello there!"
    },
    {
      "date": "2025-03-20T02:50:00Z", 
      "sender": "user2@example.com",
      "message": "How are you?"
    }
  ]
  ```

- POST / - Send message to another user
  ```bash
  curl -X POST http://localhost:8080/api/messages \
    -H "Authorization: Bearer <JWT_TOKEN>" \
    -H "Content-Type: application/json" \
    -d '{"recipient":"user2@example.com","message":"Hello from user1!"}'
  ```

### Admin (/api/admin)
- [Admin-specific endpoints]

## Frontend Pages
- login.html - User login interface
- register.html - User registration interface
- user.html - Main user interface
- admin.html - Admin management interface

## Security Overview

### Authentication & Authorization
- JWT-based authentication using JJWT library
- Role-based access control (user_role, admin_role)
- Secure password hashing using BCrypt
- Token expiration and refresh mechanisms

### Key Security Dependencies
1. **JJWT (Java JWT)**
   - Provides secure JWT creation and validation
   - Implements industry-standard JWT specifications
   - Actively maintained with regular security updates

2. **Spring Security**
   - Comprehensive security framework for Spring applications
   - Provides authentication, authorization, and protection against common vulnerabilities
   - Integrates with other Spring components seamlessly

3. **Jakarta Servlet API**
   - Standardized web application security interfaces
   - Provides secure request/response handling
   - Enables proper session management and security context

### Security Best Practices
- All sensitive data is encrypted in transit (HTTPS)
- Strong password policies enforced
- Rate limiting implemented to prevent brute force attacks
- Input validation and sanitization for all user inputs
- Regular security audits and dependency updates

### Threat Mitigation
- Protection against common web vulnerabilities (XSS, CSRF, SQLi)
- Secure session management
- Proper error handling to prevent information leakage
- Regular security testing and code reviews

For detailed dependency analysis, see [SBOM_report.md](SBOM_report.md)
