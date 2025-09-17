package com.example.library.utils;

import com.example.library.models.Book;

public class BookDTO {
    private Long id;
    private String title;

    public BookDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
}


