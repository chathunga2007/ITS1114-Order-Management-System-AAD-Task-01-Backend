# 🛒 Order Management System REST API

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✓-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-✓-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-✓-BC4521?style=for-the-badge)

**A full-featured RESTful Order Management System built with Spring Boot, supporting Admin, Cashier, and Customer roles.**

</div>

---

## 📋 Table of Contents

- [Overview](#overview)
- [System Requirements](#system-requirements)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Configuration](#database-configuration)
- [How to Run](#how-to-run)
- [User Roles & Features](#user-roles--features)
- [API Reference](#api-reference)
  - [👤 User (Employee) API](#-user-employee-api)
  - [🙍 Customer API](#-customer-api)
  - [📦 Item API](#-item-api)
  - [🧾 Order API](#-order-api)
- [Request & Response Examples](#request--response-examples)
- [Exception Handling](#exception-handling)
- [Architecture Overview](#architecture-overview)

---

## Overview

The **Order Management System** is a Spring Boot REST API backend that manages employees, customers, items, and orders. It is designed to support three distinct user roles — **Admin**, **Cashier**, and **Customer** — each with tailored access to features.

---

## System Requirements

| Requirement | Version |
|-------------|---------|
| Java JDK    | 21+     |
| Maven       | 3.6+    |
| MySQL       | 8.0+    |

---

## Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Framework    | Spring Boot 4.0.6                   |
| Security     | Spring Security & JWT (jjwt)        |
| Language     | Java 21                             |
| ORM          | Spring Data JPA (Hibernate)         |
| Database     | MySQL                               |
| Boilerplate  | Lombok                              |
| Build Tool   | Maven                               |
| Dev Tools    | Spring Boot DevTools                |

---

## Project Structure

```
OrderManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/lk/ijse/OrderManagementSystem/
│   │   │   ├── constant/
│   │   │   │   ├── CommonResponse.java       # Unified API response wrapper
│   │   │   │   ├── ResponseMessage.java      # Standard response messages
│   │   │   │   └── ResponseStatusCode.java   # Status codes (SUCCESS=0, FAIL=1)
│   │   │   ├── controller/
│   │   │   │   ├── CustomerController.java   # Customer CRUD endpoints
│   │   │   │   ├── ItemController.java       # Item CRUD + filter endpoints
│   │   │   │   ├── OrderController.java      # Order placement & retrieval
│   │   │   │   └── UserController.java       # Employee CRUD endpoints
│   │   │   ├── dto/
│   │   │   │   ├── AuthDTO.java              # Authentication payload (Credentials)
│   │   │   │   ├── CustomerDTO.java
│   │   │   │   ├── FilterOrderDTO.java       # Order view with item list
│   │   │   │   ├── ItemDTO.java
│   │   │   │   ├── OrderDTO.java
│   │   │   │   ├── PlaceOrderDTO.java        # Order placement payload
│   │   │   │   ├── UserDataDTO.java          # Login response DTO (userId + token)
│   │   │   │   └── UserDTO.java
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Item.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java            # Junction table: Order ↔ Item
│   │   │   │   └── User.java
│   │   │   ├── enumeration/
│   │   │   │   └── UserStatus.java           # ADMIN, CASHIER
│   │   │   ├── exception/
│   │   │   │   ├── AppExceptionHandler.java  # Global REST controller advice
│   │   │   │   └── CustomerException.java    # Custom runtime exception
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── ItemRepository.java       # Includes filterItems query
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   ├── OrderRepository.java      # filterOrders + getOrdersByCustomerId
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── CustomUserDetailsService.java  # Custom UserDetailsService implementation
│   │   │   │   ├── JwtAuthenticationFilter.java   # Filter to validate JWT token in HTTP header
│   │   │   │   ├── JwtUtil.java                   # Utility class to generate/validate JWTs
│   │   │   │   └── SecurityConfig.java            # Spring Security configuration and bean definitions
│   │   │   ├── service/
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── ItemService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── impl/
│   │   │   │       ├── CustomerServiceImpl.java
│   │   │   │       ├── ItemServiceImpl.java
│   │   │   │       ├── OrderServiceImpl.java
│   │   │   │       └── UserServiceImpl.java
│   │   │   └── OrderManagementSystemApplication.java
│   │   └── resources/
│   │       └── application.properties        # DB + JPA config
│   └── test/
│       └── java/lk/ijse/OrderManagementSystem/
│           └── OrderManagementSystemApplicationTests.java
└── pom.xml
```

---

## Database Configuration

Edit `src/main/resources/application.properties` to match your MySQL setup:

```properties
spring.application.name=OrderManagementSystem

# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/order_management_system?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security Properties
jwt.secret=1affa4451895bcb7166cd38c515c67671bbfa9345a66a93f290cd6b1c22927ef650900f6604ba2aeaf40a4b146e9caff9a142876fb669143e143ef3b00bce5cbcc9368424d233406b82d723a858cacc8841c38bcfb6730de8612c74917e90327afd67ff791bea2957e05b1678553c24d87bdb13d3023515e40acdd649c531b53d9130f5460be12a8dad98c31724cb85b1793575701021090abae41460f826b35c8759088c8e2ae8e4e667821baba2a16e2886392ce7a1ce30cb0c35626abd4eb56f2657738742e769d704ebab311bde1fd2177b9f250f6bd92f4f993b745ff82b340dd5a9b833152e53f28ccd3193cc7c1bdf151471a4d122fb331713d2c05db
jwt.expiration=86400000
```

> **Note:** The database `order_management_system` will be **created automatically** if it does not exist, thanks to `createDatabaseIfNotExist=true`.

---

## How to Run

### Step 1: Clone the repository

```bash
git clone https://github.com/chathunga2007/ITS-1114-Order-Management-System-Practice-Project.git
cd OrderManagementSystem
```

### Step 2: Configure the database

Update your credentials in `src/main/resources/application.properties`.

### Step 3: Build and run

```bash
# Using Maven Wrapper (recommended)
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

The application will start on: **`http://localhost:8080`**

---

## User Roles & Features

### 👨‍💼 ADMIN

| Feature                  | Endpoint                         |
|--------------------------|----------------------------------|
| Save a new Employee      | `POST /api/users`                |
| See all Employees        | `GET /api/users/all`             |
| See single Employee      | `GET /api/users/{userId}`        |
| Edit Employee details    | `PUT /api/users`                 |
| Save a new Customer      | `POST /api/customers`            |
| See all Customers        | `GET /api/customers/all`         |
| See single Customer      | `GET /api/customers/{customerId}`|
| Edit Customer details    | `PUT /api/customers`             |
| Delete Customer          | `DELETE /api/customers/{customerId}` |
| Delete Item              | `DELETE /api/items/{itemId}`     |

---

### 🏪 CASHIER

| Feature                           | Endpoint                                  |
|-----------------------------------|-------------------------------------------|
| See all Orders (filter by name)   | `GET /api/orders/filter?customerName=...` |
| See all Items (filter by name)    | `GET /api/items/filter?itemName=...`      |
| Place an Order                    | `POST /api/orders`                        |
| Delete Customer                   | `DELETE /api/customers/{customerId}`      |
| Delete Item                       | `DELETE /api/items/{itemId}`              |

---

### 🧑‍💻 CUSTOMER

| Feature                   | Endpoint                                   |
|---------------------------|--------------------------------------------|
| See all my Orders         | `GET /api/orders/customer/{customerId}`    |

---

## 🔐 Authentication & Security

All APIs (except `/api/users/login`) are protected using **Spring Security** and require a stateless **JWT (JSON Web Token)** for authorization.

### 🔑 How to Authenticate (Login)

Send a `POST` request to the login endpoint with your credentials to obtain a JWT token.

*   **Endpoint:** `POST /api/users/login`
*   **Request Body:**
    ```json
    {
      "userName": "john_doe",
      "password": "password123"
    }
    ```
*   **Response:**
    ```json
    {
      "status": 0,
      "body": {
        "userId": 1,
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
      },
      "message": "JWT Token"
    }
    ```
    *(The `body` returns `UserDataDTO` containing `userId` and the JWT `token`)*

### 🛡️ Sending Authenticated Requests

For all other protected endpoints, you must include the retrieved JWT token in the HTTP header:

```http
Authorization: Bearer <your_jwt_token_here>
```

### 🚦 Endpoint Access Control Matrix

The system enforces Role-Based Access Control (RBAC) based on the user's role (`ADMIN` or `CASHIER`):

| Endpoint Route | HTTP Method | Required Role | Description |
| :--- | :--- | :--- | :--- |
| `/api/users/login` | `POST` | *Public (None)* | Authenticate user & get JWT |
| `/api/users/**` | `POST`, `PUT`, `GET` | `ADMIN` | Manage employee accounts |
| `/api/customers/**` | `POST`, `PUT`, `GET`, `DELETE` | `ADMIN` or `CASHIER` | Manage customer records |
| `/api/items` | `POST`, `PUT` | `ADMIN` | Create or update inventory items |
| `/api/items/**` | `GET`, `DELETE` | `ADMIN` or `CASHIER` | View, filter, and delete inventory items |
| `/api/orders/**` | `POST`, `GET` | `ADMIN` or `CASHIER` | Place and view orders |

---

## API Reference

### 👤 User (Employee) API

**Base URL:** `/api/users`

---

#### `POST /api/users` — Save Employee

**Request Body:**
```json
{
  "username": "john_doe",
  "role": "ADMIN"
}
```
> `role` accepts: `ADMIN` or `CASHIER`

**Response:**
```json
{
  "status": 0,
  "body": null,
  "message": "Operation Successful..."
}
```

---

#### `PUT /api/users` — Update Employee

**Request Body:**
```json
{
  "userId": 1,
  "username": "john_updated",
  "role": "CASHIER"
}
```

**Response:**
```json
{
  "status": 0,
  "body": {
    "userId": 1,
    "username": "john_updated",
    "role": "CASHIER"
  },
  "message": "Operation Successful..."
}
```

---

#### `GET /api/users/all` — Get All Employees

**Response:**
```json
{
  "status": 0,
  "body": [
    { "userId": 1, "username": "john_doe", "role": "ADMIN" },
    { "userId": 2, "username": "jane", "role": "CASHIER" }
  ],
  "message": "Operation Successful..."
}
```

---

#### `GET /api/users/{userId}` — Get Single Employee

**Example:** `GET /api/users/1`

**Response:**
```json
{
  "status": 0,
  "body": {
    "userId": 1,
    "username": "john_doe",
    "role": "ADMIN"
  },
  "message": "Operation Successful..."
}
```

---

### 🙍 Customer API

**Base URL:** `/api/customers`

---

#### `POST /api/customers` — Save Customer

**Request Body:**
```json
{
  "customerName": "Alice Silva",
  "email": "alice@example.com",
  "phoneNumber": "0771234567"
}
```

---

#### `PUT /api/customers` — Update Customer

**Request Body:**
```json
{
  "customerId": 1,
  "customerName": "Alice Updated",
  "email": "alice_new@example.com",
  "phoneNumber": "0779999999"
}
```

---

#### `GET /api/customers/all` — Get All Customers

**Response:**
```json
{
  "status": 0,
  "body": [
    {
      "customerId": 1,
      "customerName": "Alice Silva",
      "email": "alice@example.com",
      "phoneNumber": "0771234567"
    }
  ],
  "message": "Operation Successful..."
}
```

---

#### `GET /api/customers/{customerId}` — Get Single Customer

**Example:** `GET /api/customers/1`

---

#### `DELETE /api/customers/{customerId}` — Delete Customer

**Example:** `DELETE /api/customers/1`

Deletes the customer record by customer ID.

**Response:**
```json
{
  "status": 0,
  "body": null,
  "message": "Operation Successful..."
}
```

---

### 📦 Item API

**Base URL:** `/api/items`

---

#### `POST /api/items` — Save Item

**Request Body:**
```json
{
  "itemName": "Wireless Mouse",
  "itemQTY": "50",
  "itemPrice": "1500"
}
```

---

#### `GET /api/items/all` — Get All Items

---

#### `GET /api/items/filter` — Filter Items by Name

**Example:** `GET /api/items/filter?itemName=mouse`

Returns all items whose name contains "mouse" (case-insensitive).

**Response:**
```json
{
  "status": 0,
  "body": [
    {
      "itemId": 3,
      "itemName": "Wireless Mouse",
      "itemQTY": "50",
      "itemPrice": "1500"
    }
  ],
  "message": "Operation Successful..."
}
```

> **Tip:** Call with no `itemName` param (`GET /api/items/filter`) to retrieve **all** items.

---

#### `GET /api/items/{itemId}` — Get Single Item

**Example:** `GET /api/items/3`

---

#### `PUT /api/items` — Update Item

**Request Body:**
```json
{
  "itemId": 3,
  "itemName": "Wireless Mouse Pro",
  "itemQTY": "30",
  "itemPrice": "2000"
}
```

---

#### `DELETE /api/items/{itemId}` — Delete Item

**Example:** `DELETE /api/items/3`

Deletes the item record by item ID.

**Response:**
```json
{
  "status": 0,
  "body": null,
  "message": "Operation Successful..."
}
```

---

### 🧾 Order API

**Base URL:** `/api/orders`

---

#### `POST /api/orders` — Place Order

**Request Body:**
```json
{
  "customerId": 1,
  "total": 4500.00,
  "itemIdList": [1, 3, 5]
}
```

- `customerId` — ID of the customer placing the order
- `total` — Total price of the order
- `itemIdList` — List of item IDs to include in the order

**Response:**
```json
{
  "status": 0,
  "body": null,
  "message": "Operation Successful..."
}
```

> ✅ Order is saved with the current date/time. Each `OrderItem` is created with `quantity = 1` and the item's unit price.

---

#### `GET /api/orders/filter` — Filter Orders by Customer Name

**Example:** `GET /api/orders/filter?customerName=alice`

Returns all orders where the customer name matches (partial, case-insensitive).

**Response:**
```json
{
  "status": 0,
  "body": [
    {
      "orderId": 1,
      "customerName": "Alice Silva",
      "itemList": [
        { "itemId": 1, "itemName": "Keyboard", "itemQTY": "10", "itemPrice": "3000" },
        { "itemId": 3, "itemName": "Wireless Mouse", "itemQTY": "50", "itemPrice": "1500" }
      ]
    }
  ],
  "message": "Operation Successful..."
}
```

> **Tip:** Call with no param (`GET /api/orders/filter`) to get **all** orders.

---

#### `GET /api/orders/customer/{customerId}` — Get All Orders of a Customer

**Example:** `GET /api/orders/customer/1`

Returns all orders placed by the customer with ID `1`.

**Response:**
```json
{
  "status": 0,
  "body": [
    {
      "orderId": 1,
      "customerName": "Alice Silva",
      "itemList": [
        { "itemId": 1, "itemName": "Keyboard", "itemQTY": "10", "itemPrice": "3000" }
      ]
    }
  ],
  "message": "Operation Successful..."
}
```

---

## Request & Response Examples

### Common Response Structure

All API endpoints return the same **`CommonResponse`** wrapper:

| Field     | Type    | Description                              |
|-----------|---------|------------------------------------------|
| `status`  | `int`   | `0` = success, `1` = failure             |
| `body`    | `Object`| The returned data payload (can be null)  |
| `message` | `String`| Human-readable result message            |

---

## ⚠️ Exception Handling

The application uses a global `@ControllerAdvice` (`AppExceptionHandler`) for centralized error handling across all REST endpoints:

- **Custom Exceptions (`CustomerException`)**: Returns a structured `CommonResponse` with custom status codes and error messages.
- **Unhandled Exceptions (`Exception`)**: Handles general server errors gracefully with HTTP status 500 and message `"Unexpected error occurred"`.

---

## Architecture Overview

```
┌─────────────────────────────────────┐
│           REST Client               │
│   (Postman / Frontend / Mobile)     │
└─────────────────┬───────────────────┘
                  │ HTTP Request
                  ▼
┌─────────────────────────────────────┐
│           Controller Layer          │
│  UserController / CustomerController│
│  ItemController / OrderController   │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│           Service Layer             │
│  *ServiceImpl (Business Logic)      │
│  @Transactional for placeOrder      │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│         Repository Layer            │
│  JpaRepository + Custom @Query      │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│           MySQL Database            │
│  order_management_system schema     │
│  Tables: user, customer, item,      │
│          orders, order_item         │
└─────────────────────────────────────┘
```

### Entity Relationships

```
User         (id, username, role[ADMIN|CASHIER])
Customer     (id, customerName, email, phoneNumber)
Item         (id, itemName, itemQTY, itemPrice)
Order        (id, orderDate, total, customer_id → Customer)
OrderItem    (id, orderItemQTY, orderItemPrice, orders_id → Order, item_id → Item)
```

---

## 📄 License

This project is created for educational purposes as part of the **ITS-1114** AAD module at **IJSE**.

---

<div align="center">
  Made with ❤️ Chathunga Bimsara using Spring Boot &nbsp;|&nbsp; IJSE Practice Project
</div>
