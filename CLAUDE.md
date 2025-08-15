# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin-based Spring Boot monolithic backend service for a WeChat Mini Program called "火箭绿醇" (Rocket Green
Alcohol). The service handles user authentication via WeChat's login mechanism and provides JWT-based authentication for
protected endpoints.

## Technology Stack

- Kotlin
- Spring Boot
- Gradle (multi-project build)
- PostgreSQL database
- JWT for authentication
- WeChat Mini Program integration

## Project Structure

- `monolith-service/` - Main service containing all business logic
- `migration-job/` - Database migration jobs (likely)
- Main application class: `monolith-service/src/main/kotlin/com/funnyjack/monolith/MonolithServiceApplication.kt`

## Key Components

1. **Authentication Flow**:
    - Users login via WeChat jsCode through `/users/login` endpoint (not `/userInfos/login` as previously documented)
    - Service calls WeChat API to exchange jsCode for openid
    - Generates JWT token with openid as subject
    - Protected endpoints use `@CurrentUserOpenId` annotation to extract openid from JWT

2. **Key Classes**:
    - `UserController` - Handles user CRUD operations and login endpoint
    - `UserService` - Business logic for user management and authentication
    - `CurrentUserOpenIdArgumentResolver` - Resolves JWT token to openid
    - `JwtUtil` - JWT token generation and validation
    - `LoginConstant` - WeChat API configuration
    - `User` - JPA entity for user data
    - `UserRepository` - Spring Data JPA repository for user entity

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

## Configuration

- Main config: `monolith-service/src/main/resources/application.yaml`
- Local development config: `monolith-service/src/main/resources/application-local.yaml`
- Service runs on port 8080 with `/api` context path by default

## API Endpoints

- POST `/api/users/login` - Login with WeChat jsCode
- GET `/api/users` - Get current user info (protected endpoint)
- POST `/api/users` - Create a new user
- PUT `/api/users` - Update current user info (protected endpoint)
- DELETE `/api/users` - Delete current user (protected endpoint)
- GET `/api/users/{openid}` - Get user by openid (admin endpoint)
- PUT `/api/users/{openid}` - Update user by openid (admin endpoint)
- DELETE `/api/users/{openid}` - Delete user by openid (admin endpoint)

## Authentication

Protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

The token is generated during login and contains the user's openid as the subject.

## Database

Uses PostgreSQL with JPA/Hibernate. Local development database configuration is in `application-local.yaml`.