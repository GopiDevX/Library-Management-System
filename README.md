# Library Management System - Walkthrough

## 1. Complete Java Code & Folder Structure
I have successfully built and compiled the Java project inside your work directory at:
**`c:/Projects/Console Application/Library Management System/`**

The directory structure created is:
```
src/main/java/com/library/
├── model/
│   ├── User.java, Admin.java, Borrower.java
│   ├── Book.java
│   ├── Transaction.java
│   └── Fine.java
├── repository/
│   └── DataStore.java // Singleton storing Maps and Lists
├── service/
│   ├── SessionManager.java, AuthService.java
│   ├── BookService.java
│   ├── BorrowingService.java
│   ├── FineService.java
│   └── ReportService.java
└── controller/
    ├── InputUtil.java
    ├── AuthMenu.java, AdminMenu.java, BorrowerMenu.java
    └── Main.java // Contains application start and data seeder
```

## 2. Step-by-Step Explanation
1. **Model Creation**: Encapsulated state and behavior in `Book`, abstract `User`, `Admin`, `Borrower`, `Transaction`, and `Fine`.
2. **Repository Setup**: Implemented a central `DataStore` following the Singleton pattern to keep a centralized and shared in-memory state.
3. **Service Logic**: 
   - Created **AuthService** and **SessionManager** for authentication.
   - Built **BookService** for inventory tasks.
   - Designed **FineService** to accurately apply complex tier-based rules for delays and lost entities.
   - Drafted **BorrowingService** connecting Books, Borrowers, Fines, and Transactions securely.
   - Engineered **ReportService** for aggregate Admin tracking and Borrower history.
4. **Console Interaction Layer**: Cleaned interaction with safe token reading in `InputUtil` and connected it to distinct robust menus (`AdminMenu`, `BorrowerMenu`, `AuthMenu`).
5. **Seeding and Execution**: Packaged everything into `Main.java` which initializes context, seeds required default entities (1 admin, 2 borrowers, 4 books), and triggers the infinite console loop.

## 3. Sample Run (Console Interaction)
```text
==================================
   LIBRARY MANAGEMENT SYSTEM
==================================

1. Login
2. Exit
Enter choice: 1
Email: admin@lib.com
Password: admin123
Login successful. Welcome, System Admin!

--- ADMIN MENU ---
1. Add Book
2. Modify Book Quantity
3. Delete Book
4. View All Books (Sort by Name)
5. View All Books (Sort by Available Qty)
...
9. Reports
0. Logout
Select option: 4
ISBN: 978-0134685991 | Title: Effective Java | Author: Joshua Bloch | Cost: 600.00 | Available: 5/5
...
Select option: 0
Logged out successfully.

==================================
   LIBRARY MANAGEMENT SYSTEM
==================================

1. Login
Enter choice: 1
Email: alice@lib.com
Password: pass123
Login successful. Welcome, Alice Smith!
...
--- BORROWER MENU ---
3. Add book to cart
...
Enter ISBN to add to cart: 978-0134685991
Added to cart.
5. View Cart & Checkout
Cart: [978-0134685991]
Proceed to checkout? (y/n): y
Successfully borrowed: Effective Java
```

## 4. Key Design Decisions
- **Loose Coupling**: Controllers talk to Services, Services talk to DataStore. Menus do not directly modify Models directly where there's business logic (e.g., deducting amounts).
- **Single Responsibility**: Fine calculations are extracted specifically to `FineService` instead of polluting the `BorrowingService`.
- **Inheritance vs Composition**: `User` is subclassed for Admin/Borrower accurately to distribute responsibilities. Cart is composed directly into `Borrower` since it spans the domain lifespan of the user's session without requiring persistence across sessions.
- **In-Memory Tracking Integrity**: Reductions in book's total quantity automatically reflect in reduced available counts to prevent false states.

## 5. Time Complexity
- **Lookups (HashMaps for Users/Books)**: `O(1)` average time complexity, extremely fast for login and checkout checks.
- **Transactions & Fines searches**: `O(N)` where `N` is the operations log size (Lists), utilizing Java Streams.
- **Sorting (by Name/Available Qty)**: `O(M log M)` where `M` is total unique books via the `Comparator` stream pipeline. The 'Frequently Borrowed' report algorithm groups by frequency and sorts in `O(N log N)` time.
