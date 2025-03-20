# Message Server Application

The **Message Server Application** is a secure Java web server designed to facilitate user-to-user messaging while implementing **strong security mechanisms** to prevent vulnerabilities like **unauthorized access, brute-force attacks, and data breaches**.

📌 **Purpose**
This project serves as an educational tool for developers to:
- Understand secure coding practices in **Spring Boot** applications.
- Implement **JWT authentication, password hashing, and message encryption**.
- Learn about **rate limiting, logging, and API security best practices**.

By working with this project, developers gain hands-on experience in **secure web development**.

---

## 🏗️ **Technologies Used**
- **Java 17** – Backend implementation
- **Spring Boot** – Web framework
- **H2 Database** – In-memory database for temporary storage
- **BCrypt** – Secure password hashing
- **AES Encryption** – Secure message storage
- **JJWT** – Secure JWT-based authentication
- **Spring Security** – Role-based access control
- **Postman/cURL** – API testing tools

---

## 📂 **Folder and File Structure**
```
MessageServerApplication/
│── src/
│   ├── main/
│   │   ├── java/se/gritacademy/
│   │   │   ├── MessageServerApplication.java   # Main Spring Boot application
│   │   │   ├── api/
│   │   │   │   ├── AdminController.java       # Handles admin actions
│   │   │   │   ├── MessageController.java     # Handles messaging logic
│   │   │   │   ├── UserController.java        # Manages user operations
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java        # Manages authentication
│   │   │   ├── model/
│   │   │   │   ├── Message.java               # Represents message entity
│   │   │   │   ├── UserInfo.java              # Stores user info (email, role, hashed password)
│   │   │   ├── repository/
│   │   │   │   ├── MessageRepository.java     # Message data access
│   │   │   │   ├── UserRepository.java        # User data access
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java           # Handles authentication logic
│   │   │   │   ├── MessageService.java        # Business logic for messages
│   │   │   │   ├── UserService.java           # Manages user operations
│   │   │   ├── util/
│   │   │   │   ├── JwtUtil.java               # JWT management
│   │   │   │   ├── LoggerUtil.java            # Application logging
│   │   │   │   ├── EncryptionUtil.java        # AES encryption for messages
│   │   │   │   ├── RateLimiter.java           # Prevents brute-force attacks
│   │   ├── resources/
│   │   │   ├── application.properties         # Configurations
│   │   │   ├── static/
│   │   │   │   ├── admin.html                 # Admin interface
│   │   │   │   ├── login.html                 # Login page
│   │   │   │   ├── register.html              # Registration page
│   │   │   │   ├── user.html                  # User dashboard
│── README.md                                  # Project documentation
```

---

## 🚀 **Building and Running the Application**

### 🛠 1. Clone the Repository
```bash
git clone https://github.com/mahsel79/MessageServer.git  
cd MessageServer
```

### 🏗 2. Build and Run the Application
```bash
./mvnw spring-boot:run  
```
OR (if using Maven installed on your system)
```bash
mvn spring-boot:run  
```

### 🌍 3. Access the Application
- **Frontend:** Open [http://localhost:8080/](http://localhost:8080/) in a browser.
- **API Endpoints:** Use **Postman** or **cURL** to interact with the backend.

---

## 📡 **API Endpoints**
### 🛠 **Authentication (/api)**
| Endpoint       | HTTP Method | Description |
|---------------|------------|-------------|
| `/register`   | POST       | Register new user |
| `/login`      | POST       | Authenticate user and return JWT |
| `/logout`     | GET        | Logs out user (JWT invalidation not required) |
| `/users`      | GET        | List all registered users |

### ✉️ **Messages (/api/messages)**
| Endpoint          | HTTP Method | Description |
|------------------|------------|-------------|
| `/messages`      | GET        | Retrieve all messages for logged-in user |
| `/messages`      | POST       | Send a message to another user |

### 🔑 **Admin (/api/admin)**
| Endpoint       | HTTP Method | Description |
|---------------|------------|-------------|
| `/block`      | POST       | Block/unblock a user |

---

## 🔒 **Security Features Implemented**

### ✅ **Authentication & Authorization**
- **JWT authentication** ensures secure login/logout.
- **Role-based access control** (admin/user roles).

### ✅ **Password Security**
- **BCrypt password hashing** prevents storing plaintext passwords.
- **Password policy enforced** (min. 12 chars, uppercase, lowercase, digit, special char).

### ✅ **Message Encryption**
- **AES encryption** ensures messages are stored securely and decrypted only when needed.

### ✅ **Rate Limiting & Brute Force Protection**
- **RateLimiter** restricts excessive login attempts.

### ✅ **Logging & Auditing**
- **LoggerUtil** records login attempts, message retrieval, and admin actions.

---

## 🔥 **Security Vulnerabilities Addressed**

### ❌ **SQL Injection (SQLi)**
✅ **Fixed by using ORM (`JpaRepository`) instead of raw SQL queries.**

### ❌ **Cross-Site Scripting (XSS)**
✅ **Sanitized user input to prevent JavaScript injection.**

### ❌ **Broken Authentication**
✅ **Implemented JWT with expiration & signature verification.**

### ❌ **Sensitive Data Exposure**
✅ **Passwords are stored securely with hashing.**
✅ **Messages are encrypted before storage.**

---

## 📌 **Next Steps**
✔️ Implement **Spring Security** for enhanced access control.  
✔️ Prevent **sending messages to blocked users**.  
✔️ Add **admin user deletion functionality**.  
✔️ Conduct **security penetration testing**.

---

## ⚠️ **Disclaimer**
This project is for **educational purposes only**. Do not deploy this application in a **production environment** without removing vulnerabilities and performing security hardening.

