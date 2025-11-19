# User Management Service

A lightweight Java-based User Management Service providing RESTful APIs for user lifecycle operations (create, read, update, delete, list). Designed to be simple to run locally, easy to test, and container-ready with Docker.

## Table of contents
- [Features](#features)
- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
- [Prerequisites](#prerequisites)
- [Build](#build)
- [Build with Docker](#build-with-docker)
- [Run locally](#run-locally)
- [Configuration](#configuration)
- [API](#api)
- [ENDPOINT](#endpoint)
- [SWAGGER UI](#swagger-ui)


## Features
- Basic CRUD operations for users
- RESTful JSON API
- Validation and error handling
- Ready to run in a Docker container

## Tech stack
- Java
- Maven
- Docker

## Getting started

### Prerequisites
- Java 21
- Maven 3.x
- Docker

### Build
The project uses Maven as the build tool.
From the repository root:

- Clean and build:
  mvn clean package

This will produce an executable JAR in target/ (app.jar)

### Build with Docker
- Start the application: docker compose up

### Run locally
Run the built jar:

java -jar target/app.jar

The service will start and listen on the configured port

## Configuration
The service reads configuration from:
- application.yml

Common configuration items:
- SERVER_PORT (default 8080)
- DATABASE_URL / jdbc:postgresql://localhost:5432/user_db
- DATASOURCE_USERNAME / postgres
- DATASOURCE_PASSWORD / 12345

## API
Use the base URL depending on where you run the service:
- Local: http://localhost:8080/api/users
- Render: https://usermanagementservice-0w9o.onrender.com/health
- Render: https://usermanagementservice-0w9o.onrender.com/api/users

## ENDPOINT
- POST /api/users
    - Create a new user
    - Request body: {"username": "EvaBaker", "email": "eva.baker@example.com", "phoneNumber": "+358401234567", "role": "ADMIN"}
    - Response: created user resource with id

- GET /api/users
    - List users
    - Query params: page, size, sort

- GET /api/users/{id}
    - Get a single user by id

- PUT /api/users/{id}
    - Update user by id
    - Request body: (fields to update)

- DELETE /api/users/{id}
    - Delete user by id

Add example curl calls:
- Create
  curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "EvaBaker", "email": "eva.baker@example.com", "phoneNumber": "+358401234567", "role": "ADMIN"}'

- Get list
  curl http://localhost:8080/api/users

# SWAGGER UI
## http://localhost:8080/swagger-ui/index.html

## https://usermanagementservice-0w9o.onrender.com/swagger-ui/index.html