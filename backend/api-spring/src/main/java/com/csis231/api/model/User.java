package com.csis231.api.model;

import jakarta.persistence.*;

@Table(name ="user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_ID;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    public User() {}
    public User(String email, String username, String password) {
        super();
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public String getUserName() {
        return username;
    }
    public void setName(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}