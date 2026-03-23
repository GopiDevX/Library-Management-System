package com.library.controller;

import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.ReportService;
import com.library.model.Book;
import com.library.model.Transaction;

import java.util.List;

public class AdminMenu {
    private AuthService authService;
    private BookService bookService;
    private ReportService reportService;

    public AdminMenu(AuthService authService, BookService bookService, ReportService reportService) {
        this.authService = authService;
        this.bookService = bookService;
        this.reportService = reportService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Book");
            System.out.println("2. Modify Book Quantity");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books (Sort by Name)");
            System.out.println("5. View All Books (Sort by Available Qty)");
            System.out.println("6. Search Book by Name");
            System.out.println("7. Search Book by ISBN");
            System.out.println("8. Manage Users (Register new)");
            System.out.println("9. Reports");
            System.out.println("0. Logout");

            int choice = InputUtil.readInt("Select option: ");

            switch (choice) {
                case 1: addBook(); break;
                case 2: modifyBook(); break;
                case 3: deleteBook(); break;
                case 4: printBooks(bookService.getAllBooksSortedByName()); break;
                case 5: printBooks(bookService.getAllBooksSortedByAvailableQuantity()); break;
                case 6: searchByName(); break;
                case 7: searchByIsbn(); break;
                case 8: manageUsers(); break;
                case 9: reportsMenu(); break;
                case 0:
                    authService.logout();
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addBook() {
        String isbn = InputUtil.readString("ISBN: ");
        String title = InputUtil.readString("Title: ");
        String author = InputUtil.readString("Author: ");
        double cost = InputUtil.readDouble("Cost: ");
        int quantity = InputUtil.readInt("Quantity: ");
        
        if (bookService.addBook(isbn, title, author, cost, quantity)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Book with this ISBN already exists.");
        }
    }

    private void modifyBook() {
        String isbn = InputUtil.readString("Enter ISBN: ");
        int qty = InputUtil.readInt("Enter new total quantity: ");
        if (bookService.modifyBookQuantity(isbn, qty)) {
            System.out.println("Quantity updated.");
        } else {
            System.out.println("Failed to update. Verify ISBN and ensure total doesn't fall below borrowed copies.");
        }
    }

    private void deleteBook() {
        String isbn = InputUtil.readString("Enter ISBN: ");
        if (bookService.deleteBook(isbn)) {
            System.out.println("Book deleted.");
        } else {
            System.out.println("Cannot delete. Book doesn't exist or copies are currently borrowed.");
        }
    }

    private void searchByName() {
        String title = InputUtil.readString("Enter title to search: ");
        printBooks(bookService.searchByName(title));
    }

    private void searchByIsbn() {
        String isbn = InputUtil.readString("Enter ISBN: ");
        Book b = bookService.searchByIsbn(isbn);
        if (b != null) {
            System.out.println(b);
        } else {
            System.out.println("Book not found.");
        }
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void manageUsers() {
        System.out.println("1. Add Admin\n2. Add Borrower");
        int type = InputUtil.readInt("Choice: ");
        String name = InputUtil.readString("Name: ");
        String email = InputUtil.readString("Email: ");
        String pass = InputUtil.readString("Password: ");
        
        boolean success = false;
        if (type == 1) success = authService.registerAdmin(name, email, pass);
        else if (type == 2) success = authService.registerBorrower(name, email, pass);
        else System.out.println("Invalid choice.");
        
        if (success) System.out.println("User registered successfully.");
        else System.out.println("Registration failed. Email might exist.");
    }

    private void reportsMenu() {
        System.out.println("\n--- REPORTS ---");
        System.out.println("1. Low Stock Books");
        System.out.println("2. Never Borrowed Books");
        System.out.println("3. Frequently Borrowed Books (Top by frequency)");
        System.out.println("4. Students with Overdue Books");
        System.out.println("5. Book Status by ISBN");
        
        int choice = InputUtil.readInt("Select report: ");
        switch (choice) {
            case 1:
                int threshold = InputUtil.readInt("Enter low stock threshold: ");
                printBooks(reportService.getLowStockBooks(threshold));
                break;
            case 2:
                printBooks(reportService.getNeverBorrowedBooks());
                break;
            case 3:
                System.out.println("Frequently borrowed ISBNs (sorted descending):");
                reportService.getFrequentlyBorrowedBooks().forEach(isbn -> {
                    Book b = bookService.searchByIsbn(isbn);
                    if (b != null) System.out.println(b.getTitle() + " (" + isbn + ")");
                });
                break;
            case 4:
                List<String> overdue = reportService.getStudentsWithOverdueBooks();
                if (overdue.isEmpty()) System.out.println("No overdue books.");
                else overdue.forEach(System.out::println);
                break;
            case 5:
                String isbn = InputUtil.readString("Enter ISBN: ");
                List<Transaction> activeTx = reportService.getBookStatusByIsbn(isbn);
                if (activeTx.isEmpty()) {
                    System.out.println("No active borrowings for this ISBN.");
                } else {
                    activeTx.forEach(t -> System.out.println("Borrowed by: " + t.getBorrowerEmail() + " | Expected Return: " + t.getDueDate()));
                }
                break;
        }
    }
}
