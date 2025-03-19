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
- POST /login - User login
- GET /logout - User logout
- GET /users - List all users (requires authentication)

### Messages (/api/messages)
- GET / - Get messages for authenticated user
- POST / - Send message to another user

### Admin (/api/admin)
- [Admin-specific endpoints]

## Frontend Pages
- login.html - User login interface
- register.html - User registration interface
- user.html - Main user interface
- admin.html - Admin management interface

## Security
- JWT-based authentication
- Role-based access control (user_role, admin_role)
- Password hashing using BCrypt
