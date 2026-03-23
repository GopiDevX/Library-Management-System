package com.library.controller;

import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.model.Transaction;
import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.BorrowingService;
import com.library.service.FineService;
import com.library.service.ReportService;

import java.util.List;

public class BorrowerMenu {
    private AuthService authService;
    private BookService bookService;
    private BorrowingService borrowingService;
    private FineService fineService;
    private ReportService reportService;

    public BorrowerMenu(AuthService authService, BookService bookService, BorrowingService borrowingService, FineService fineService, ReportService reportService) {
        this.authService = authService;
        this.bookService = bookService;
        this.borrowingService = borrowingService;
        this.fineService = fineService;
        this.reportService = reportService;
    }

    public void showMenu(Borrower currentBorrower) {
        while (true) {
            System.out.println("\n--- BORROWER MENU ---");
            System.out.println("1. View All Books");
            System.out.println("2. Search Book");
            System.out.println("3. Add book to cart");
            System.out.println("4. Remove book from cart");
            System.out.println("5. View Cart & Checkout");
            System.out.println("6. Return Book");
            System.out.println("7. Extend Tenure");
            System.out.println("8. Mark Book as Lost");
            System.out.println("9. Report Lost Membership Card");
            System.out.println("10. View Fines & Pay");
            System.out.println("11. View Fine History");
            System.out.println("12. View Borrow History");
            System.out.println("0. Logout");

            int choice = InputUtil.readInt("Select option: ");

            switch (choice) {
                case 1:
                    bookService.getAllBooksSortedByName().forEach(System.out::println);
                    break;
                case 2:
                    String query = InputUtil.readString("Enter title or ISBN: ");
                    Book b = bookService.searchByIsbn(query);
                    if (b != null) System.out.println(b);
                    else bookService.searchByName(query).forEach(System.out::println);
                    break;
                case 3:
                    String addIsbn = InputUtil.readString("Enter ISBN to add to cart: ");
                    if (borrowingService.addToCart(currentBorrower, addIsbn)) {
                        System.out.println("Added to cart.");
                    } else {
                        System.out.println("Failed to add. Checks: ISBN validity, availability, max limit (3), or already in cart/borrowed.");
                    }
                    break;
                case 4:
                    String remIsbn = InputUtil.readString("Enter ISBN to remove from cart: ");
                    if (borrowingService.removeFromCart(currentBorrower, remIsbn)) {
                        System.out.println("Removed from cart.");
                    } else {
                        System.out.println("Book not in cart.");
                    }
                    break;
                case 5:
                    System.out.println("Cart: " + currentBorrower.getCartBooks());
                    String conf = InputUtil.readString("Proceed to checkout? (y/n): ");
                    if (conf.equalsIgnoreCase("y")) {
                        System.out.println(borrowingService.checkout(currentBorrower));
                    }
                    break;
                case 6:
                    showActiveBorrowings(currentBorrower);
                    String retId = InputUtil.readString("Enter Transaction ID to return: ");
                    if (borrowingService.returnBook(currentBorrower, retId)) {
                        System.out.println("Book returned.");
                    } else {
                        System.out.println("Invalid Transaction ID or already returned.");
                    }
                    break;
                case 7:
                    showActiveBorrowings(currentBorrower);
                    String extId = InputUtil.readString("Enter Transaction ID to extend: ");
                    if (borrowingService.extendTenure(currentBorrower, extId)) {
                        System.out.println("Tenure extended by 15 days.");
                    } else {
                        System.out.println("Cannot extend. Max extensions reached or invalid ID.");
                    }
                    break;
                case 8:
                    showActiveBorrowings(currentBorrower);
                    String lostId = InputUtil.readString("Enter Transaction ID of lost book: ");
                    if (borrowingService.markBookLost(currentBorrower, lostId)) {
                        System.out.println("Marked as lost. Fine assessed.");
                    } else {
                        System.out.println("Invalid Transaction ID.");
                    }
                    break;
                case 9:
                    fineService.processLostMembershipCard(currentBorrower);
                    System.out.println("Reported lost card. Fine assessed.");
                    break;
                case 10:
                    payFines(currentBorrower);
                    break;
                case 11:
                    reportService.getFineHistory(currentBorrower.getEmail()).forEach(f -> 
                        System.out.println("Date: " + f.getDateAssessed() + " | Amount: " + f.getAmount() + " | Reason: " + f.getReason() + " | Paid: " + f.isPaid()));
                    break;
                case 12:
                    reportService.getBorrowHistory(currentBorrower.getEmail()).forEach(t -> 
                        System.out.println("TxID: " + t.getId() + " | ISBN: " + t.getBookIsbn() + " | Borrow Date: " + t.getBorrowDate() + " | Status: " + t.getStatus()));
                    break;
                case 0:
                    authService.logout();
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showActiveBorrowings(Borrower borrower) {
        List<Transaction> activeTx = borrowingService.getActiveTransactions(borrower.getEmail());
        if (activeTx.isEmpty()) {
            System.out.println("No active borrowings.");
        } else {
            activeTx.forEach(t -> {
                Book b = bookService.searchByIsbn(t.getBookIsbn());
                System.out.println("TxID: " + t.getId() + " | Book: " + (b != null ? b.getTitle() : "Unknown") + " | Due: " + t.getDueDate() + " | Extensions: " + t.getExtensionCount());
            });
        }
    }

    private void payFines(Borrower borrower) {
        List<Fine> unpaidFines = fineService.getUnpaidFines(borrower.getEmail());
        if (unpaidFines.isEmpty()) {
            System.out.println("No unpaid fines.");
            return;
        }

        double total = unpaidFines.stream().mapToDouble(Fine::getAmount).sum();
        System.out.println("Total unpaid fine amount: ₹" + total);
        System.out.println("Your deposit balance: ₹" + borrower.getDepositBalance());
        
        System.out.println("1. Pay specific fine by cash");
        System.out.println("2. Pay specific fine from deposit");
        System.out.println("0. Go back");
        int choice = InputUtil.readInt("Select payment option: ");
        
        if (choice == 1 || choice == 2) {
            unpaidFines.forEach(f -> System.out.println("FineID: " + f.getId() + " | Amount: " + f.getAmount() + " | Reason: " + f.getReason()));
            String fineId = InputUtil.readString("Enter FineID to pay: ");
            Fine targetFine = unpaidFines.stream().filter(f -> f.getId().equals(fineId)).findFirst().orElse(null);
            
            if (targetFine != null) {
                boolean useDeposit = (choice == 2);
                if (fineService.payFine(targetFine, borrower, useDeposit)) {
                    System.out.println("Fine paid successfully.");
                } else {
                    System.out.println("Insufficient deposit.");
                }
            } else {
                System.out.println("Invalid FineID.");
            }
        }
    }
}
