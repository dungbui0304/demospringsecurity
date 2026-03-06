# 📊 SO SÁNH 2 CÁCH TẠO TOKEN

## **Cách 1: AuthServerConfig (OAuth2 Authorization Server)**
### **Dùng khi:**
- API gọi API (machine-to-machine)
- Microservices architecture
- Third-party integrations
- IoT devices

### **Cách gọi:**
```bash
# 1. Lấy token
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=client-id" \
  -d "client_secret=client-secret"

# 2. Gọi API
curl -X GET http://localhost:8080/api/persons/paged \
  -H "Authorization: Bearer {token}"
```

### **Ưu điểm:**
✅ Chuẩn OAuth2 (industry standard)
✅ Không cần user database
✅ Token không liên kết với user cụ thể
✅ Dễ scale cho microservices

### **Nhược điểm:**
❌ Không biết user nào đang gọi
❌ Không thể revoke token của user cụ thể

---

## **Cách 2: LoginController (Custom JWT)**
### **Dùng khi:**
- Web applications
- Mobile apps
- User authentication
- Cần biết user info

### **Cách gọi:**
```bash
# 1. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass"}'

# 2. Gọi API
curl -X GET http://localhost:8080/api/persons/paged \
  -H "Authorization: Bearer {token}"
```

### **Ưu điểm:**
✅ Biết chính xác user nào đang gọi
✅ Có thể load user info từ database
✅ Có thể revoke token của user
✅ Linh hoạt customize

### **Nhược điểm:**
❌ Không chuẩn OAuth2
❌ Phức tạp hơn
❌ Cần maintain user database

---

## **Kết luận:**
- **Muốn API service** → Dùng **AuthServerConfig**
- **Muốn user authentication** → Dùng **LoginController**
- **Có thể dùng cả 2** trong cùng app!