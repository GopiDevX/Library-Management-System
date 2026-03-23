package com.library.controller;

import com.library.model.Admin;
import com.library.model.Borrower;
import com.library.model.User;
import com.library.service.AuthService;
import com.library.service.SessionManager;

public class AuthMenu {
    private AuthService authService;
    private SessionManager sessionManager;
    private AdminMenu adminMenu;
    private BorrowerMenu borrowerMenu;

    public AuthMenu(AuthService authService, AdminMenu adminMenu, BorrowerMenu borrowerMenu) {
        this.authService = authService;
        this.sessionManager = SessionManager.getInstance();
        this.adminMenu = adminMenu;
        this.borrowerMenu = borrowerMenu;
    }

    public void start() {
        System.out.println("\n==================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==================================");
        
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            int choice = InputUtil.readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void handleLogin() {
        String email = InputUtil.readString("Email: ");
        String password = InputUtil.readString("Password: ");
        
        if (authService.login(email, password)) {
            User currentUser = sessionManager.getCurrentUser();
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
            
            if (currentUser instanceof Admin) {
                adminMenu.showMenu();
            } else if (currentUser instanceof Borrower) {
                borrowerMenu.showMenu((Borrower) currentUser);
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }
}
