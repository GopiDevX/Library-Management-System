package com.library.controller;

import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.BorrowingService;
import com.library.service.FineService;
import com.library.service.ReportService;

public class Main {
    public static void main(String[] args) {
        // Initialize Services
        AuthService authService = new AuthService();
        BookService bookService = new BookService();
        FineService fineService = new FineService();
        BorrowingService borrowingService = new BorrowingService(fineService);
        ReportService reportService = new ReportService();

        // Initialize Menus
        AdminMenu adminMenu = new AdminMenu(authService, bookService, reportService);
        BorrowerMenu borrowerMenu = new BorrowerMenu(authService, bookService, borrowingService, fineService, reportService);
        AuthMenu authMenu = new AuthMenu(authService, adminMenu, borrowerMenu);

        // Seed Data
        seedData(authService, bookService);

        // Start Application
        authMenu.start();
    }

    private static void seedData(AuthService authService, BookService bookService) {
        // Seed 1 Admin
        authService.registerAdmin("System Admin", "admin@lib.com", "admin123");

        // Seed 2 Borrowers
        authService.registerBorrower("Alice Smith", "alice@lib.com", "pass123");
        authService.registerBorrower("Bob Jones", "bob@lib.com", "pass123");

        // Seed Books
        bookService.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 600.0, 5);
        bookService.addBook("978-0596009205", "Head First Java", "Kathy Sierra", 450.0, 3);
        bookService.addBook("978-0201633610", "Design Patterns", "Gang of Four", 750.0, 2);
        bookService.addBook("978-0137081073", "Clean Coder", "Robert C. Martin", 500.0, 4);
    }
}
