package com.example.library.models;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user") // unikaj nazwy USER bo może kolidować
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // UserId

    @Column(nullable = false, unique = true)
    private String userName; // login (może być nazwa lub mail)

    @Column(nullable = false)
    private String passwordHash; // zaszyfrowane hasło (BCrypt)

    @Column(nullable = true, unique = true)
    private String email; // UserMail

    @Column(nullable = true)
    private String displayName; // UserName - do wyświetlania

    @Column(nullable = false)
    private String role = "USER"; // "USER" lub "ADMIN"

    // konstruktor bezargumentowy
    public User() {}

    // getters / setters
    // ... wygeneruj w IDE
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

