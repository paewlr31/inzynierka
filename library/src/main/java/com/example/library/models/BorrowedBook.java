package com.example.library.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // który użytkownik
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime dueAt;

    // jaka książka
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime borrowedAt;
    
    @Column(nullable = false)
    private boolean extensionUsed = false;

    public BorrowedBook() {}

    public BorrowedBook(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowedAt = LocalDateTime.now();
        this.dueAt = this.borrowedAt.plusDays(60);
    }

    // getters / setters
    public LocalDateTime getDueAt() { return dueAt; }  // <-- getter
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; } // <-- setter
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public LocalDateTime getBorrowedAt() { return borrowedAt; }
    public void setBorrowedAt(LocalDateTime borrowedAt) { this.borrowedAt = borrowedAt; }
    public boolean isExtensionUsed() {
        return extensionUsed;
    }

    public void setExtensionUsed(boolean extensionUsed) {
        this.extensionUsed = extensionUsed;
    }
}
