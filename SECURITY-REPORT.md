# Security Report

## **1. Implemented Security Mechanisms**

### **1.1 JWT Authentication**

📍 **Location:** `src/main/java/se/gritacademy/util/JwtUtil.java`

#### **Implementation Details:**

- Uses **JSON Web Tokens (JWT)** with **HMAC SHA-256 (HS256)** signature.
- Tokens include:
  - **Subject (email)** → **Line 24**
  - **Issued At (iat)** → **Line 25**
  - **Expiration (exp)** → **Line 26** (**24-hour validity**)
- Token validation includes:
  - **Signature verification** → **Line 34**
  - **Expiration check** → **Line 38**
  - **Malformed token detection** → **Line 40**

#### **Security Technique:**

- **Asymmetric cryptography using HMAC SHA-256**
- **Prevents token tampering and ensures authenticity**
- **Short expiration time reduces impact of token compromise**

#### **Alternative Approaches:**

- Use **RSA signatures** for better key management.
- Implement **refresh tokens** for enhanced security.
- Add **additional claims** for granular access control.

#### **🔹 Example API Calls using cURL:**

**Login & Get Token:**

```powershell
$response = curl -X POST http://localhost:8080/api/login `
  -H "Content-Type: application/json" `
  -d '{"email":"user@example.com","password":"SecurePassword123"}'

$token = ($response | ConvertFrom-Json).token
```

---

### **1.2 Message Encryption**

📍 **Location:** `src/main/java/se/gritacademy/util/EncryptionUtil.java`

#### **Implementation Details:**

- Uses **AES encryption** with **128-bit key**.
- **Base64 encoding** ensures safe storage.
- **Encryption/Decryption methods:**
  - `encrypt()` → **Line 12**
  - `decrypt()` → **Line 25**
- **Key generation from static secret** → **Line 36**

#### **Security Technique:**

- **Symmetric encryption using AES**
- **Protects message confidentiality at rest**
- **Base64 encoding ensures safe storage in the database**

#### **Alternative Approaches:**

- Use a **key management system** instead of a hardcoded key.
- Implement a **key rotation strategy**.
- Use **authenticated encryption (AEAD)** for better security.

#### **🔹 Example API Calls using cURL:**

**Send an Encrypted Message:**

```powershell
curl -X POST "http://localhost:8080/api/messages" `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"recipient":"user2@example.com","message":"Hello from user1!"}'
```

**Retrieve Encrypted Messages:**

```powershell
curl -X GET http://localhost:8080/api/messages `
  -H "Authorization: Bearer $token"
```

---

### **1.3 Rate Limiting**

📍 **Location:** `src/main/java/se/gritacademy/util/RateLimiter.java`

#### **Implementation Details:**

- **Limits API requests per user/IP**.
- Uses **sliding window algorithm**.
- **Configurable limits and window sizes**.
- **Logs rate limit events**.

#### **Security Technique:**

- **Request throttling**
- **Prevents brute force and DoS attacks**
- **Protects API availability**

#### **Alternative Approaches:**

- Use **distributed rate limiting** for scalability.
- Implement **adaptive rate limiting**.
- Add **CAPTCHA for suspicious traffic**.

#### **🔹 Example API Calls using cURL:**

**Trigger Rate Limit on Login Attempts:**

```powershell
1..10 | ForEach-Object {
  curl -X POST http://localhost:8080/api/login `
    -H "Content-Type: application/json" `
    -d '{"email":"user@example.com","password":"wrongpassword"}'
}
```

---

### **1.4 Secure Configuration**

📍 **Location:** `src/main/resources/application.properties`

#### **Implementation Details:**

- **Externalized JWT secret configuration**.
- **Accessed via `System.getenv()`** in `JwtUtil.java`.
- **Prevents hardcoding of sensitive data**.

#### **Security Technique:**

- **Environment-based configuration**
- **Reduces risk of secret exposure**
- **Enables easy rotation of secrets**

#### **Alternative Approaches:**

- Use a **dedicated secret management service**.
- Implement **secret rotation automation**.
- Add **secret versioning**.

---

### **1.5 Password Policy**

📍 **Location:** `src/main/java/se/gritacademy/service/AuthService.java`

#### **Implementation Details:**

- Enforces **password complexity requirements**:
  - **Minimum 12 characters**
  - **At least one uppercase and one lowercase letter**
  - **At least one number and one special character**
- Uses **secure password hashing with BCrypt**.
- **Prevents weak password usage**.

#### **Security Technique:**

- **Strong password enforcement**
- **Protects against brute force attacks**
- **Reduces risk of credential stuffing**

#### **Alternative Approaches:**

- Implement **password breach checking**.
- Add **password expiration policy**.
- Enable **multi-factor authentication (MFA)**.

#### **🔹 Example API Calls using cURL:**

**Register a New User:**

```powershell
curl -X POST http://localhost:8080/api/register `
  -H "Content-Type: application/json" `
  -d '{"email":"user@example.com","password":"SecurePassword123!"}'
```

**Login with Strong Password:**

```powershell
$response = curl -X POST http://localhost:8080/api/login `
  -H "Content-Type: application/json" `
  -d '{"email":"user@example.com","password":"SecurePassword123!"}'

$token = ($response | ConvertFrom-Json).token
```

---

### **📌 Summary of Security Enhancements**

✅ **JWT tokens include expiration & signature verification**
✅ **Passwords are hashed & stored securely**
✅ **Messages are encrypted at rest**
✅ **Rate limiting prevents brute-force attacks**
✅ **Secrets are stored securely in environment variables**
✅ **Strict password policy is enforced**
