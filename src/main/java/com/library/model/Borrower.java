package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {
    private double depositBalance;
    private boolean lostMembershipCard;
    private List<String> cartBooks; // Store ISBNs of books in cart

    public Borrower(String name, String email, String password) {
        super(name, email, password);
        this.depositBalance = 1500.0;
        this.lostMembershipCard = false;
        this.cartBooks = new ArrayList<>();
    }

    public double getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(double depositBalance) {
        this.depositBalance = depositBalance;
    }

    public void deductBalance(double amount) {
        this.depositBalance -= amount;
    }
    
    public void addBalance(double amount) {
        this.depositBalance += amount;
    }

    public boolean hasLostMembershipCard() {
        return lostMembershipCard;
    }

    public void setLostMembershipCard(boolean lostMembershipCard) {
        this.lostMembershipCard = lostMembershipCard;
    }

    public List<String> getCartBooks() {
        return cartBooks;
    }

    public void addToCart(String isbn) {
        if (!cartBooks.contains(isbn)) {
            cartBooks.add(isbn);
        }
    }

    public void removeFromCart(String isbn) {
        cartBooks.remove(isbn);
    }

    public void clearCart() {
        cartBooks.clear();
    }
}
