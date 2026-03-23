package com.library.service;

import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Transaction;
import com.library.repository.DataStore;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowingService {
    private DataStore dataStore;
    private FineService fineService;
    private static final int MAX_BORROW_LIMIT = 3;
    private static final double MIN_BALANCE_REQUIRED = 500.0;
    private static final int DEFAULT_BORROW_DAYS = 15;

    public BorrowingService(FineService fineService) {
        this.dataStore = DataStore.getInstance();
        this.fineService = fineService;
    }

    public List<Transaction> getActiveTransactions(String email) {
        return dataStore.getTransactions().stream()
                .filter(t -> t.getBorrowerEmail().equals(email) && t.getStatus() == Transaction.TransactionStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public boolean addToCart(Borrower borrower, String isbn) {
        Book book = dataStore.getBooks().get(isbn);
        if (book == null || book.getAvailableQuantity() <= 0) {
            return false; // Book not available
        }
        
        List<Transaction> activeTx = getActiveTransactions(borrower.getEmail());
        
        // Cannot borrow same book twice
        boolean alreadyBorrowed = activeTx.stream().anyMatch(t -> t.getBookIsbn().equals(isbn));
        if (alreadyBorrowed || borrower.getCartBooks().contains(isbn)) {
            return false;
        }

        // Check limits
        if (activeTx.size() + borrower.getCartBooks().size() >= MAX_BORROW_LIMIT) {
            return false;
        }

        borrower.addToCart(isbn);
        return true;
    }

    public boolean removeFromCart(Borrower borrower, String isbn) {
        if (borrower.getCartBooks().contains(isbn)) {
            borrower.removeFromCart(isbn);
            return true;
        }
        return false;
    }

    public String checkout(Borrower borrower) {
        if (borrower.getDepositBalance() < MIN_BALANCE_REQUIRED) {
            return "Checkout failed: Minimum security balance of ₹500 required.";
        }
        if (borrower.getCartBooks().isEmpty()) {
            return "Checkout failed: Cart is empty.";
        }

        StringBuilder result = new StringBuilder();
        for (String isbn : borrower.getCartBooks()) {
            Book book = dataStore.getBooks().get(isbn);
            if (book != null && book.getAvailableQuantity() > 0) {
                book.reduceAvailableQuantity(1);
                Transaction transaction = new Transaction(
                        borrower.getEmail(), isbn, LocalDate.now(), DEFAULT_BORROW_DAYS);
                dataStore.getTransactions().add(transaction);
                result.append("Successfully borrowed: ").append(book.getTitle()).append("\n");
            } else {
                result.append("Failed to borrow (unavailable): ").append(isbn).append("\n");
            }
        }
        borrower.clearCart();
        return result.toString();
    }

    public boolean returnBook(Borrower borrower, String transactionId) {
        Transaction transaction = dataStore.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId) && t.getBorrowerEmail().equals(borrower.getEmail()))
                .findFirst().orElse(null);

        if (transaction == null || transaction.getStatus() != Transaction.TransactionStatus.ACTIVE) {
            return false;
        }

        Book book = dataStore.getBooks().get(transaction.getBookIsbn());
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(Transaction.TransactionStatus.RETURNED);
        
        if (book != null) {
            book.increaseAvailableQuantity(1);
            fineService.processReturnFine(transaction, book, borrower);
        }
        return true;
    }

    public boolean extendTenure(Borrower borrower, String transactionId) {
        Transaction transaction = dataStore.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId) && t.getBorrowerEmail().equals(borrower.getEmail()))
                .findFirst().orElse(null);

        if (transaction == null || transaction.getStatus() != Transaction.TransactionStatus.ACTIVE) {
            return false;
        }

        if (transaction.getExtensionCount() >= 2) {
            return false; // Max extensions reached
        }

        transaction.setDueDate(transaction.getDueDate().plusDays(DEFAULT_BORROW_DAYS));
        transaction.incrementExtensionCount();
        return true;
    }

    public boolean markBookLost(Borrower borrower, String transactionId) {
        Transaction transaction = dataStore.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId) && t.getBorrowerEmail().equals(borrower.getEmail()))
                .findFirst().orElse(null);

        if (transaction == null || transaction.getStatus() != Transaction.TransactionStatus.ACTIVE) {
            return false;
        }

        Book book = dataStore.getBooks().get(transaction.getBookIsbn());
        transaction.setStatus(Transaction.TransactionStatus.LOST);
        transaction.setReturnDate(LocalDate.now());
        
        if (book != null) {
            // Book is lost, reduce total quantity
            book.setTotalQuantity(book.getTotalQuantity() - 1);
            fineService.processLostBookFine(book, borrower);
        }
        return true;
    }
}
