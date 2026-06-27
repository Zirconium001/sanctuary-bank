# Sanctuary Bank

**Sanctuary of Equilibrium** — a digital banking platform backend built with Spring Boot, providing core banking operations including customer onboarding, multi-product accounts, transactions, fixed deposits, loans, and tiered customer benefits.

> ⚠️ This is a backend project — there is no UI included. Interact with it via REST API calls (Postman, curl, or your own frontend).

---

## Features

- **Customer onboarding & authentication** — registration, login, JWT-based session handling
- **Multiple account types** — Savings, Checking, Fixed Deposit, Loan
- **Core transactions** — deposit, withdraw, transfer, transaction history
- **Fixed Deposits** — creation, premature closure (with penalty), maturity payout
- **Loans** — application, automatic approval/disbursal, repayment
- **Customer tiering** — Regular / Silver / Gold / Platinum, with tier-based daily withdrawal & transfer limits
- **Interest rate cards** — published rates for savings, FDs, and loans
- **Admin operations** — system stats, monthly interest application, FD maturity processing, tier upgrades
- **PIN-protected accounts** with lockout after repeated failed attempts

---

## Tech Stack

| Layer          | Technology                                  |
|----------------|----------------------------------------------|
| Language       | Java 17                                       |
| Framework      | Spring Boot 3.2.4 (Web, Data JPA, Security, Validation) |
| Database       | PostgreSQL                                    |
| Auth           | JWT (`jjwt` 0.12.5) + Spring Security, stateless sessions |
| Build tool     | Maven                                         |
| Containerized  | Docker (multi-stage build)                    |
| Hosting        | [Render](https://render.com)                  |

---

## Project Structure

```
src/main/java/com/sanctuary/
├── controller/      # REST endpoints (Auth, Account, Customer, Transaction, FD, Loan, Rate, Admin)
├── domain/
│   ├── account/      # Account hierarchy: Savings, Checking, FixedDeposit, Loan
│   ├── customer/     # Customer, CustomerTier, KycStatus
│   ├── bank/         # BankInfo, Branch
│   ├── product/      # InterestRateCard
│   └── transaction/  # TransactionRecord, TransactionType
├── dto/              # Request/response payloads
├── repository/       # Spring Data JPA repositories
├── service/          # Business logic (Account, Customer, Transaction, Loan, Interest, Compliance)
├── security/         # JWT token provider + auth filter
├── config/           # SecurityConfig (CORS, stateless auth, route rules)
├── exception/        # Custom banking exceptions + global handler
└── util/             # ID generation, date utils, input sanitization
```

---


## API Reference

All responses are wrapped in:
```json
{ "success": true, "message": "...", "data": { ... } }
```

### Auth — `/api/auth`
| Method | Endpoint    | Description              | Auth |
|--------|-------------|---------------------------|------|
| POST   | `/register` | Register a new customer   | No   |
| POST   | `/login`    | Login, returns JWT        | No   |

### Customers — `/api/customers`
| Method | Endpoint         | Description                  | Auth |
|--------|------------------|-------------------------------|------|
| GET    | `/me`            | Get current customer profile  | Yes  |
| PUT    | `/me/address`    | Update address                | Yes  |
| GET    | `/me/accounts`   | List accounts for customer    | Yes  |

### Accounts — `/api/accounts`
| Method | Endpoint                    | Description                 | Auth |
|--------|------------------------------|------------------------------|------|
| POST   | `/savings`                   | Open a savings account       | Yes  |
| POST   | `/checking`                  | Open a checking account      | Yes  |
| GET    | `/{accountNumber}`            | Get account details          | Yes  |
| POST   | `/{accountNumber}/balance`    | Check balance (PIN required) | Yes  |
| PUT    | `/{accountNumber}/pin`        | Change account PIN           | Yes  |
| DELETE | `/{accountNumber}`            | Close account                | Yes  |
| POST   | `/{accountNumber}/reopen`     | Reopen a closed account      | Yes  |

### Transactions — `/api/transactions`
| Method | Endpoint                          | Description              | Auth |
|--------|------------------------------------|---------------------------|------|
| POST   | `/deposit`                          | Deposit funds              | Yes  |
| POST   | `/withdraw`                         | Withdraw funds (PIN)       | Yes  |
| POST   | `/transfer`                         | Transfer between accounts  | Yes  |
| GET    | `/history/{accountNumber}`          | Full transaction history    | Yes  |
| GET    | `/history/{accountNumber}/recent`   | Last 10 transactions        | Yes  |

### Fixed Deposits — `/api/fd`
| Method | Endpoint                              | Description              | Auth |
|--------|-----------------------------------------|----------------------------|------|
| POST   | `/create`                                | Open a fixed deposit        | Yes  |
| POST   | `/{accountNumber}/premature-close`       | Close FD before maturity    | Yes  |
| POST   | `/{accountNumber}/mature`                | Process matured FD payout   | Yes  |

### Loans — `/api/loans`
| Method | Endpoint                  | Description                | Auth |
|--------|----------------------------|------------------------------|------|
| POST   | `/apply`                    | Apply for a loan             | Yes  |
| POST   | `/{accountNumber}/repay`    | Make a loan repayment        | Yes  |

### Rates — `/api/rates`
| Method | Endpoint     | Description           | Auth |
|--------|--------------|-------------------------|------|
| GET    | `/savings`    | Savings interest rates  | No   |
| GET    | `/fd`         | Fixed deposit rates     | No   |
| GET    | `/loans`      | Loan interest rates     | No   |

### Admin — `/api/admin`
| Method | Endpoint               | Description                          | Auth |
|--------|--------------------------|----------------------------------------|------|
| GET    | `/stats`                  | Customer/account/transaction counts    | No*  |
| POST   | `/apply-interest`         | Apply monthly savings interest          | No*  |
| POST   | `/process-matured-fds`    | Close all matured FDs                   | No*  |
| POST   | `/upgrade-tiers`          | Re-evaluate and upgrade customer tiers  | No*  |

\* Open per current `SecurityConfig` — see security note above.

---


## Customer Tiers

| Tier      | Min. Relationship Value | Daily Withdrawal Limit | Daily Transfer Limit |
|-----------|--------------------------|--------------------------|------------------------|
| Regular   | ₹0                        | ₹50,000                   | ₹100,000                |
| Silver    | ₹50,000                   | ₹200,000                  | ₹500,000                |
| Gold      | ₹200,000                  | ₹500,000                  | ₹2,000,000               |
| Platinum  | ₹500,000                  | ₹2,000,000                | ₹10,000,000              |

---

## Roadmap / Ideas

- Restrict `/api/admin/**` to an authenticated admin role
- Externalize all secrets/credentials from `application.yml`
- Add automated tests (currently only `spring-boot-starter-test` scaffold is present)
- Add API documentation via OpenAPI/Swagger
- Add a scheduled job for monthly interest application instead of manual admin trigger

---
