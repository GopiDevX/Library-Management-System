package com.library.repository;

import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Transaction;
import com.library.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private static DataStore instance;

    private Map<String, User> users; // Key: Email
    private Map<String, Book> books; // Key: ISBN
    private List<Transaction> transactions;
    private List<Fine> fines;

    private DataStore() {
        users = new HashMap<>();
        books = new HashMap<>();
        transactions = new ArrayList<>();
        fines = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Book> getBooks() {
        return books;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Fine> getFines() {
        return fines;
    }
}
