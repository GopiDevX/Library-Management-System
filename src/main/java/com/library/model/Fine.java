package com.library.model;

import java.time.LocalDate;
import java.util.UUID;

public class Fine {
    private String id;
    private String borrowerEmail;
    private double amount;
    private String reason;
    private LocalDate dateAssessed;
    private boolean isPaid;

    public Fine(String borrowerEmail, double amount, String reason) {
        this.id = UUID.randomUUID().toString();
        this.borrowerEmail = borrowerEmail;
        this.amount = amount;
        this.reason = reason;
        this.dateAssessed = LocalDate.now();
        this.isPaid = false;
    }

    public String getId() {
        return id;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getDateAssessed() {
        return dateAssessed;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
