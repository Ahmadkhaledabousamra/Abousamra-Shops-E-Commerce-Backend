# Secure E-Commerce Backend API

A secure and scalable **RESTful e-commerce backend** built with **Spring Boot**.  
This project demonstrates real-world backend development practices including authentication, authorization, and modular architecture.

---

## 🚀 Tech Stack

- **Java**
- **Spring Boot**
- **Spring Security**
- **JWT Authentication**
- **Hibernate / JPA**
- **MySQL**
- **Maven**

---

## ✨ Features

- JWT-based authentication and authorization
- Role-based access control
- User management
- Product and category management
- Shopping cart functionality
- Order processing system
- Secure RESTful APIs
- Global exception handling
- Clean layered architecture (Controller / Service / Repository)

---

## 🧩 Project Structure

src/
├── controller # REST controllers
├── service # Business logic
├── repository # JPA repositories
├── model # Entities
├── dto # Data Transfer Objects
├── security # JWT & Spring Security configuration
└── exception # Global exception handling


---

## 🔐 Authentication Flow

1. User registers or logs in
2. Server generates a **JWT token**
3. Client sends the token in the `Authorization` header
4. Spring Security validates the token for protected endpoints
