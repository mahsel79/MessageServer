# Security Report

## 1. Implemented Security Mechanisms

### 1.1 JWT Authentication

**Location:** src/main/java/se/gritacademy/util/JwtUtil.java

**Implementation Details:**

- Uses JSON Web Tokens (JWT) with HMAC SHA-256 (HS256) signature
- Tokens include:
  - Subject (email) - Line 24
  - Issued At (iat) - Line 25
  - Expiration (exp) - Line 26 (24 hours)
- Token validation includes:
  - Signature verification - Line 34
  - Expiration check - Line 38
  - Malformed token detection - Line 40

**Security Technique:**

- Asymmetric cryptography using HMAC SHA-256
- Prevents token tampering and ensures authenticity
- Short expiration time reduces impact of token compromise

**Alternative Approaches:**

- Could use RSA signatures for better key management
- Could implement refresh tokens for better security
- Could add additional claims for more granular access control

### 1.2 Message Encryption

**Location:** src/main/java/se/gritacademy/util/EncryptionUtil.java

**Implementation Details:**

- Uses AES encryption with 128-bit key
- Base64 encoding for safe storage
- Encryption/Decryption methods:
  - encrypt() - Line 12
  - decrypt() - Line 25
- Key generation from static secret - Line 36

**Security Technique:**

- Symmetric encryption using AES
- Protects message confidentiality at rest
- Base64 encoding ensures safe storage in database

**Alternative Approaches:**

- Use key management system instead of hardcoded key
- Implement key rotation strategy
- Use authenticated encryption (AEAD) for better security

### 1.3 Rate Limiting

**Location:** src/main/java/se/gritacademy/util/RateLimiter.java

**Implementation Details:**

- Limits API requests per user/IP
- Uses sliding window algorithm
- Configurable limits and window sizes
- Logs rate limit events

**Security Technique:**

- Request throttling
- Prevents brute force and DoS attacks
- Protects API availability

**Alternative Approaches:**

- Use distributed rate limiting for scalability
- Implement adaptive rate limiting
- Add CAPTCHA for suspicious traffic

### 1.4 Secure Configuration

**Location:** src/main/resources/application.properties

**Implementation Details:**

- Externalized JWT secret configuration
- Accessed via @Value annotation - Line 13 in JwtUtil.java
- Prevents hardcoding of sensitive data

**Security Technique:**

- Environment-based configuration
- Reduces risk of secret exposure
- Enables easy rotation of secrets

**Alternative Approaches:**

- Use dedicated secret management service
- Implement secret rotation automation
- Add secret versioning

### 1.5 Password Policy

**Location:** src/main/java/se/gritacademy/service/AuthService.java

**Implementation Details:**

- Enforces password requirements:
  - Minimum 12 characters
  - Mixed case letters
  - Numbers and special characters
- Uses secure password hashing
- Prevents weak password usage

**Security Technique:**

- Password complexity requirements
- Protects against brute force attacks
- Reduces risk of credential stuffing

**Alternative Approaches:**

- Implement password breach checking
- Add password expiration policy
- Enable multi-factor authentication
