package com.library.service;

import com.library.model.Admin;
import com.library.model.Borrower;
import com.library.model.User;
import com.library.repository.DataStore;

public class AuthService {
    private DataStore dataStore;
    private SessionManager sessionManager;

    public AuthService() {
        this.dataStore = DataStore.getInstance();
        this.sessionManager = SessionManager.getInstance();
    }

    public boolean login(String email, String password) {
        User user = dataStore.getUsers().get(email);
        if (user != null && user.getPassword().equals(password)) {
            sessionManager.login(user);
            return true;
        }
        return false;
    }

    public void logout() {
        sessionManager.logout();
    }

    public boolean registerAdmin(String name, String email, String password) {
        if (dataStore.getUsers().containsKey(email)) {
            return false;
        }
        Admin admin = new Admin(name, email, password);
        dataStore.getUsers().put(email, admin);
        return true;
    }

    public boolean registerBorrower(String name, String email, String password) {
        if (dataStore.getUsers().containsKey(email)) {
            return false;
        }
        Borrower borrower = new Borrower(name, email, password);
        dataStore.getUsers().put(email, borrower);
        return true;
    }
}
