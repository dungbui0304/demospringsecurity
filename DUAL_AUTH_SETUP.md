# 🚀 DEMO: TRIỂN KHAI CẢ 2 CÁCH AUTHENTICATION

## **Cấu hình hoàn thành:**
✅ AuthServerConfig - OAuth2 Client Credentials
✅ LoginController - User Authentication với JWT
✅ ResourceServerConfig - Xử lý cả 2 loại token
✅ JWKSource - Key chung để sign/verify tokens

---

## **Test Cách 1: OAuth2 Client Credentials**

```bash
# 1. Lấy token từ OAuth2 server
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=client-id" \
  -d "client_secret=client-secret"

# Response: {"access_token": "eyJ...", "token_type": "Bearer"}

# 2. Gọi API với token
curl -X GET http://localhost:8080/api/persons/paged \
  -H "Authorization: Bearer eyJ..."
```

---

## **Test Cách 2: User Login**

```bash
# 1. Login để lấy token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test", "password": "test"}'

# Response: {"data": {"accessToken": "eyJ..."}}

# 2. Gọi API với token
curl -X GET http://localhost:8080/api/persons/paged \
  -H "Authorization: Bearer eyJ..."
```

---

## **Cả 2 token đều hoạt động với cùng API!**

### **Cách 1 Token (OAuth2):**
- Không chứa user info
- Scope: read, write
- Dành cho machine-to-machine

### **Cách 2 Token (JWT):**
- Chứa username trong subject
- Scope: read write
- Dành cho user authentication

---

## **Debug Token:**
Thêm log trong `JwtDebugFilter` để xem token nhận được:
```
📍 Token nhận được từ client: eyJ...
📍 Endpoint được gọi: GET /api/persons/paged
```