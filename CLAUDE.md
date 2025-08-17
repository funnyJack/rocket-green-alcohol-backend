# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin-based Spring Boot monolithic backend service for a WeChat Mini Program called "火箭绿醇" (Rocket Green
Alcohol). The service handles user authentication via WeChat's login mechanism and provides JWT-based authentication for
protected endpoints. It also manages user contracts/orders with different contract types.

## Technology Stack

- Kotlin
- Spring Boot
- Gradle (multi-project build)
- PostgreSQL database
- JWT for authentication
- WeChat Mini Program integration
- JPA/Hibernate for ORM
- Spring Data JPA for repository patterns

## Project Structure

- `monolith-service/` - Main service containing all business logic
- `migration-job/` - Database migration jobs
- Main application class: `monolith-service/src/main/kotlin/com/funnyjack/monolith/MonolithServiceApplication.kt`

## Key Components

### Authentication Flow

1. Users login via WeChat jsCode through `/api/users/login` endpoint
2. Service calls WeChat API to exchange jsCode for openid
3. Generates JWT token with openid as subject
4. Protected endpoints use `@CurrentUserOpenId` annotation to extract openid from JWT

### Domain Entities

1. **User** - Main entity representing users with fields:
    - openid (unique identifier from WeChat)
    - is_super_admin (admin flag)
    - avatarUrl, nickname, phoneNumber, address (user profile info)
    - currentContractType (user's current contract level)
    - orders (one-to-many relationship with Order entities)

2. **Order** - Entity representing user contracts with fields:
    - user (many-to-one relationship with User)
    - contractType (enum: HYDROGEN_FRIENDS, CONTRACTED_OWNER, GREEN_ALCOHOL_PIONEER, GREEN_ALCOHOL_PARTNERS)

### Key Classes

- `UserController` - Handles user CRUD operations and login endpoint
- `UserService` - Business logic for user management and authentication
- `OrderController` - Handles order/contract CRUD operations
- `OrderService` - Business logic for order management
- `CurrentUserOpenIdArgumentResolver` - Resolves JWT token to openid
- `JwtUtil` - JWT token generation and validation
- `LoginConstant` - WeChat API configuration
- `User` - JPA entity for user data
- `UserRepository` - Spring Data JPA repository for user entity
- `Order` - JPA entity for order/contract data
- `OrderRepository` - Spring Data JPA repository for order entity

### Key Features

1. **User Management**:
    - CRUD operations for users
    - Admin endpoints for managing other users
    - Super admin protection for sensitive operations
    - User search functionality (admin only)

2. **Authentication**:
    - WeChat login integration
    - JWT token generation and validation
    - Custom argument resolver for extracting user from token

3. **Order/Contract Management**:
    - CRUD operations for user contracts
    - Different contract types with specific benefits

## Development Commands

### Build Project
```bash
./gradlew build
```

### Run Service Locally
```bash
./gradlew :monolith-service:bootRun
```

### Run Tests
```bash
./gradlew test
```

### Run Specific Module Tests
```bash
./gradlew :monolith-service:test
```

### Run Specific Test

```bash
./gradlew :monolith-service:test --tests "com.funnyjack.monolith.SomeTestClass"
```

## Configuration

- Main config: `monolith-service/src/main/resources/application.yaml`
- Local development config: `monolith-service/src/main/resources/application-local.yaml`
- Service runs on port 8080 with `/api` context path by default
- Local database: PostgreSQL on localhost:54321 with database name `rocket_green_alcohol`

## API Endpoints

### User Endpoints
- POST `/api/users/login` - Login with WeChat jsCode
- GET `/api/users` - Get current user info (protected endpoint)
- POST `/api/users` - Create a new user
- PUT `/api/users` - Update current user info (protected endpoint)
- DELETE `/api/users` - Delete current user (protected endpoint)
- GET `/api/users/{id}` - Get user by ID (admin endpoint)
- PUT `/api/users/{openid}` - Update user by openid (admin endpoint)
- DELETE `/api/users/{id}` - Delete user by ID (admin endpoint)
- POST `/api/users/search` - Search users with filters (admin endpoint)

### Order Endpoints

- GET `/api/orders` - Get current user's orders (protected endpoint)
- POST `/api/orders` - Create a new order for current user (protected endpoint)
- GET `/api/orders/{id}` - Get specific order by ID (protected endpoint)
- PUT `/api/orders/{id}` - Update specific order by ID (protected endpoint)
- DELETE `/api/orders/{id}` - Delete specific order by ID (protected endpoint)

## Authentication

Protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

The token is generated during login and contains the user's openid as the subject.

## Database

Uses PostgreSQL with JPA/Hibernate. Local development database configuration is in `application-local.yaml`.
Tables include:

- `user` - User information with WeChat openid as unique identifier
- `order` - User contract/orders with contract type information