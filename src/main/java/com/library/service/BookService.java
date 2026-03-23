package com.library.service;

import com.library.model.Book;
import com.library.repository.DataStore;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private DataStore dataStore;

    public BookService() {
        this.dataStore = DataStore.getInstance();
    }

    public boolean addBook(String isbn, String title, String author, double cost, int quantity) {
        if (dataStore.getBooks().containsKey(isbn)) {
            return false; // Book with ISBN already exists
        }
        Book newBook = new Book(isbn, title, author, cost, quantity);
        dataStore.getBooks().put(isbn, newBook);
        return true;
    }

    public boolean modifyBookQuantity(String isbn, int newTotalQuantity) {
        Book book = dataStore.getBooks().get(isbn);
        if (book == null || newTotalQuantity < (book.getTotalQuantity() - book.getAvailableQuantity())) {
            return false; // Cannot reduce total below currently borrowed copies
        }
        book.setTotalQuantity(newTotalQuantity);
        return true;
    }

    public boolean deleteBook(String isbn) {
        Book book = dataStore.getBooks().get(isbn);
        if (book != null && book.getTotalQuantity() == book.getAvailableQuantity()) {
            dataStore.getBooks().remove(isbn);
            return true;
        }
        return false; // Cannot delete if copies are borrowed or book doesn't exist
    }

    public List<Book> getAllBooksSortedByName() {
        return dataStore.getBooks().values().stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooksSortedByAvailableQuantity() {
        return dataStore.getBooks().values().stream()
                .sorted(Comparator.comparingInt(Book::getAvailableQuantity).reversed())
                .collect(Collectors.toList());
    }

    public List<Book> searchByName(String titleQuery) {
        return dataStore.getBooks().values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(titleQuery.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Book searchByIsbn(String isbn) {
        return dataStore.getBooks().get(isbn);
    }
}
