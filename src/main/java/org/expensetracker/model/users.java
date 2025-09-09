package org.expensetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class users {

    @Id
    private String username;
    private String password_hash;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password_hash;
    }

    public void setPassword(String password) {
        this.password_hash = password;
    }
}
