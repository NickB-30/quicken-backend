# Quicken Backend Internship Project

A Spring Boot REST API for managing accounts and transactions with financial summary calculations.

## Project Overview

This application provides APIs to:
- List all accounts
- Get financial summaries for accounts within date ranges
- View daily breakdowns of income and expenses

## Project Structure

```
src/
├── main/
│   ├── java/com/nicolas/quicken/
│   │   ├── QuickenApplication.java       # Main application class
│   │   ├── model/
│   │   │   ├── Account.java              # Account entity
│   │   │   └── Transaction.java          # Transaction entity
│   │   ├── repository/
│   │   │   ├── AccountRepository.java    # JDBC queries for accounts
│   │   │   └── TransactionRepository.java # JDBC queries for transactions
│   │   ├── service/
│   │   │   └── AccountService.java       # Business logic & calculations
│   │   └── controller/
│   │       └── AccountController.java    # REST API endpoints
│   └── resources/
│       ├── application.properties        # Database configuration
│       └── quicken_project.sql           # Database schema & sample data
└── test/
    └── java/com/nicolas/quicken/
        └── service/
            └── AccountServiceTest.java   # Unit tests
```

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Git

### Clone the Repository

```bash
git clone git@github.com:NickB-30/quicken-backend.git
cd quicken-backend
```

### Build the Project

```bash
./gradlew build
```

## Running the Application

```bash
./gradlew bootRun
```

The application will start on **http://localhost:8080**

## API Endpoints

### 1. Get All Accounts

**Endpoint:** `GET /api/accounts`

**Description:** Returns a list of all accounts

**Example Request:**
```bash
curl http://localhost:8080/api/accounts
```

**Example Response:**
```json
[
  {
    "id": 1,
    "name": "Personal Finances 2024",
    "description": "Personal income and household expenses"
  },
  {
    "id": 2,
    "name": "Small Business 2024",
    "description": "Simple small business cashflow"
  }
]
```

### 2. Get Account Summary

**Endpoint:** `GET /api/accounts/{accountId}/summary?from=YYYY-MM-DD&to=YYYY-MM-DD`

**Description:** Returns total income, expenses, and net for an account within a date range

**Parameters:**
- `accountId` (path) - The account ID
- `from` (query) - Start date (YYYY-MM-DD)
- `to` (query) - End date (YYYY-MM-DD)

**Example Request:**
```bash
curl "http://localhost:8080/api/accounts/1/summary?from=2024-01-01&to=2024-12-31"
```

**Example Response:**
```json
{
  "totalIncome": 7400.00,
  "totalExpenses": 3902.80,
  "net": 3497.20
}
```

### 3. Get Daily Summary (Bonus Feature)

**Endpoint:** `GET /api/accounts/{accountId}/daily-summary?from=YYYY-MM-DD&to=YYYY-MM-DD`

**Description:** Returns a daily breakdown of income, expenses, and net for each day in the date range

**Parameters:**
- `accountId` (path) - The account ID
- `from` (query) - Start date (YYYY-MM-DD)
- `to` (query) - End date (YYYY-MM-DD)

**Example Request:**
```bash
curl "http://localhost:8080/api/accounts/1/daily-summary?from=2024-01-01&to=2024-01-05"
```

**Example Response:**
```json
[
  {
    "date": "2024-01-01",
    "income": 3500,
    "expenses": 0,
    "net": 3500
  },
  {
    "date": "2024-01-02",
    "income": 0,
    "expenses": 1500,
    "net": -1500
  },
  {
    "date": "2024-01-05",
    "income": 0,
    "expenses": 220.45,
    "net": -220.45
  }
]
```

## Running Tests

Run all unit tests:

```bash
./gradlew test
```

**Test Coverage:**
- Account summary with mixed income and expenses
- Account summary with no transactions (edge case)
- Account summary with only income (edge case)
- Daily summary with transactions across multiple days
- Account summary for single day date range (boundary edge case)

All tests use Mockito to mock repository dependencies and focus on testing the business logic in the service layer.

## Design Decisions

### Why BigDecimal for Money?

`BigDecimal` is used for all financial amounts to ensure precise calculations without floating-point errors, which is critical for financial applications.

### Architecture

The application follows a clean **Controller → Service → Repository** layered architecture:
- **Controllers** handle HTTP requests/responses
- **Services** contain business logic and calculations
- **Repositories** execute SQL queries using JdbcTemplate

### Lombok for Code Simplification
Lombok annotations (@Data, @AllArgsConstructor, @NoArgsConstructor) are used throughout the project to automatically generate getters, setters, constructors, and other boilerplate code. This keeps the codebase clean and maintainable while reducing repetitive code.

## Author

**Nicolas Beringer**  
Backend Internship Project - Quicken

---

*This project was created as part of the Quicken Backend Internship application process.*
