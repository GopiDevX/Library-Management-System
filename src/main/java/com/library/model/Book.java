package com.library.model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private double cost;
    private int totalQuantity;
    private int availableQuantity;

    public Book(String isbn, String title, String author, double cost, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.cost = cost;
        this.totalQuantity = quantity;
        this.availableQuantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        int diff = totalQuantity - this.totalQuantity;
        this.totalQuantity = totalQuantity;
        this.availableQuantity += diff;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void reduceAvailableQuantity(int count) {
        this.availableQuantity -= count;
    }

    public void increaseAvailableQuantity(int count) {
        this.availableQuantity += count;
    }

    @Override
    public String toString() {
        return String.format("ISBN: %s | Title: %s | Author: %s | Cost: %.2f | Available: %d/%d", 
                             isbn, title, author, cost, availableQuantity, totalQuantity);
    }
}
