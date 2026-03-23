# 📚 Library Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

A complete, production-ready, monolithic console-based Library Management System built entirely in **Java** strictly following **Object-Oriented Programming (OOP)** principles and **Clean Architecture**.

This project simulates real-world library behaviors, handling complex workflows such as cart-based borrowing, dynamic fine calculation, temporal extensions, and analytical reporting—all backed by an efficient in-memory database configuration.

---

## ✨ Key Features

### 🛡️ Core System Architecture
- **Clean N-Tier Architecture**: Robust separation of concerns amongst `Models`, `Repositories`, `Services`, and `Controllers`.
- **In-Memory Data Store**: Utilizes the JVM `Collections` framework (`HashMaps`/`ArrayLists`) engineered via the Singleton pattern for fast, reliable state tracking during application lifecycle.
- **Robust Authentication**: Role-based access control protecting and branching **Admin** and **Borrower** menus securely.

### 👑 Admin Capabilities
- **Inventory Management**: Add, safely modify quantitative supply, and remove books.
- **Catalog Navigation**: View books sorted dynamically by Name or Available Quantity. Search robustly by exact ISBN or overlapping Keyword fields.
- **User Management**: Directly onboard new System Admins or Library Borrowers.
- **Advanced Metrics & Reporting**:
  - Alert on low-stock items based on threshold.
  - Identify never-borrowed vs. frequently borrowed catalog entries.
  - Track students with overdue items and their real-time expected return status.

### 🎓 Borrower Capabilities
- **Shopping Cart System**: Stage books (up to 3) in a unified, session-based cart before final batch-checkout.
- **Account Economy**: Starts with a ₹1500 deposit initialization, demanding a minimum ₹500 security threshold for checkout.
- **Fine Ecosystem**:
  - **Overdue Books:** 15-day grace period. Exponential fine generation scaling across a tier list (capped safely at 80% of total book cost).
  - **Loss Management:** Dedicated workflows restricting lost books (50% replacement cost assessed) and membership cards (₹10 flat fee processing).
- **Checkout Mobility**: Options to instantly return or dynamically extend usage tenure precisely from the dashboard. Fine deductions can dynamically process from the deposit balance or direct cash overrides.

---

## 🏗️ Project Structure
```text
src/main/java/com/library/
├── model/           # Data definitions (Book, User, Transaction, Fine, etc.)
├── repository/      # In-memory storage (DataStore)
├── service/         # Business logic (Auth, Book, Borrowing, Fines, Reports)
└── controller/      # CLI Interface (Menus, Entry point, I/O handling)
```

## 🚀 Getting Started

### Prerequisites
- [Java Development Kit (JDK) 8+](https://www.oracle.com/java/technologies/downloads/) installed locally.

### Installation & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/GopiDevX/Library-Management-System.git
   cd "Library-Management-System"
   ```

2. **Compile the source files:**
   Since this focuses on pure Java implementation without bloat, no external build tools are required. Compile the source tree directly:
   ```bash
   javac -sourcepath src/main/java src/main/java/com/library/controller/Main.java
   ```

3. **Run the application:**
   Launch the compiled `Main` entry controller:
   ```bash
   java -cp src/main/java com.library.controller.Main
   ```

---

## 🧪 Testing with Seed Data
To help you immediately explore the full depth of the system features, the application locally preloads the following credentials into memory on initialization:

**Admin Defaults:**
- Email: `admin@lib.com` | Password: `admin123`

**Borrower Defaults:**
- Email: `alice@lib.com` | Password: `pass123`
- Email: `bob@lib.com` | Password: `pass123`

*(A handful of development books spanning different subjects and costs are seeded to the inventory as well).*
