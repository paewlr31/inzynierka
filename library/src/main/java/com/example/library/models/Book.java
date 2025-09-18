package com.example.library.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 2000)
    private String description;

    @Column(nullable = true)
    private Integer year;

    @Lob
    @Column(name = "image", nullable = true)
    private byte[] image; // obrazek w bazie danych
    
    private LocalDateTime createdAt;

    public Book() {}
    
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    
}
