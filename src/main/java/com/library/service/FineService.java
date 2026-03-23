package com.library.service;

import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Fine;
import com.library.model.Transaction;
import com.library.repository.DataStore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class FineService {
    private DataStore dataStore;
    private static final int MAX_REGULAR_DAYS = 15;

    public FineService() {
        this.dataStore = DataStore.getInstance();
    }

    public void processReturnFine(Transaction transaction, Book book, Borrower borrower) {
        LocalDate today = LocalDate.now();
        LocalDate dueGracePeriod = transaction.getDueDate().plusDays(MAX_REGULAR_DAYS);
        
        if (today.isAfter(dueGracePeriod)) {
            long lateDays = ChronoUnit.DAYS.between(dueGracePeriod, today);
            double fineAmount = calculateDelayFine(lateDays, book.getCost());
            if (fineAmount > 0) {
                Fine fine = new Fine(borrower.getEmail(), fineAmount, "Late return: " + book.getTitle());
                dataStore.getFines().add(fine);
            }
        }
    }

    public void processLostBookFine(Book book, Borrower borrower) {
        double fineAmount = book.getCost() * 0.50;
        Fine fine = new Fine(borrower.getEmail(), fineAmount, "Lost book: " + book.getTitle());
        dataStore.getFines().add(fine);
    }

    public void processLostMembershipCard(Borrower borrower) {
        borrower.setLostMembershipCard(true);
        Fine fine = new Fine(borrower.getEmail(), 10.0, "Lost membership card");
        dataStore.getFines().add(fine);
    }

    private double calculateDelayFine(long lateDays, double bookCost) {
        double totalFine = 0.0;
        double currentRate = 2.0;

        for (long i = 1; i <= lateDays; i++) {
            totalFine += currentRate;
            if (i % 10 == 0) {
                currentRate *= 2; // exponential increase every 10 days
            }
        }

        double maxFine = bookCost * 0.80;
        return Math.min(totalFine, maxFine);
    }

    public List<Fine> getUnpaidFines(String email) {
        return dataStore.getFines().stream()
                .filter(f -> f.getBorrowerEmail().equals(email) && !f.isPaid())
                .collect(Collectors.toList());
    }

    public double getTotalUnpaidFines(String email) {
        return getUnpaidFines(email).stream().mapToDouble(Fine::getAmount).sum();
    }

    public boolean payFine(Fine fine, Borrower borrower, boolean useDeposit) {
        if (useDeposit) {
            if (borrower.getDepositBalance() >= fine.getAmount()) {
                borrower.deductBalance(fine.getAmount());
                fine.setPaid(true);
                return true;
            }
            return false; // Insufficient deposit
        } else {
            // Pay by cash
            fine.setPaid(true);
            return true;
        }
    }
}
