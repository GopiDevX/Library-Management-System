package com.library.model;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private String id;
    private String borrowerEmail;
    private String bookIsbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
    private int extensionCount;

    public enum TransactionStatus {
        ACTIVE,     // Currently borrowed
        RETURNED,   // Successfully returned
        LOST        // Book marked as lost
    }

    public Transaction(String borrowerEmail, String bookIsbn, LocalDate borrowDate, int daysToDue) {
        this.id = UUID.randomUUID().toString();
        this.borrowerEmail = borrowerEmail;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(daysToDue);
        this.status = TransactionStatus.ACTIVE;
        this.extensionCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public int getExtensionCount() {
        return extensionCount;
    }

    public void incrementExtensionCount() {
        this.extensionCount++;
    }
}
