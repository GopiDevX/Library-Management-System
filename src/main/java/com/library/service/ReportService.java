package com.library.service;

import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Transaction;
import com.library.repository.DataStore;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {
    private DataStore dataStore;

    public ReportService() {
        this.dataStore = DataStore.getInstance();
    }

    // Admin Reports

    public List<Book> getLowStockBooks(int threshold) {
        return dataStore.getBooks().values().stream()
                .filter(b -> b.getAvailableQuantity() < threshold)
                .collect(Collectors.toList());
    }

    public List<Book> getNeverBorrowedBooks() {
        List<String> borrowedIsbns = dataStore.getTransactions().stream()
                .map(Transaction::getBookIsbn)
                .distinct()
                .collect(Collectors.toList());

        return dataStore.getBooks().values().stream()
                .filter(b -> !borrowedIsbns.contains(b.getIsbn()))
                .collect(Collectors.toList());
    }

    public List<String> getFrequentlyBorrowedBooks() {
        // Returns ISBNs sorted by frequency
        return dataStore.getTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getBookIsbn, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    public List<String> getStudentsWithOverdueBooks() {
        LocalDate now = LocalDate.now();
        return dataStore.getTransactions().stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ACTIVE && now.isAfter(t.getDueDate()))
                .map(Transaction::getBorrowerEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Transaction> getBookStatusByIsbn(String isbn) {
        return dataStore.getTransactions().stream()
                .filter(t -> t.getBookIsbn().equals(isbn) && t.getStatus() == Transaction.TransactionStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    // Borrower Reports

    public List<Fine> getFineHistory(String email) {
        return dataStore.getFines().stream()
                .filter(f -> f.getBorrowerEmail().equals(email))
                .collect(Collectors.toList());
    }

    public List<Transaction> getBorrowHistory(String email) {
        return dataStore.getTransactions().stream()
                .filter(t -> t.getBorrowerEmail().equals(email))
                .collect(Collectors.toList());
    }
}
