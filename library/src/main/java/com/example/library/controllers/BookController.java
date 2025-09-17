package com.example.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.repository.BookRepository;
import com.example.library.utils.BookDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookRepository bookRepository;

    // wstrzykiwanie repozytorium przez konstruktor
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // endpoint zwracający wszystkie książki
    @GetMapping("/books")
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle()))
                .collect(Collectors.toList());
    }



}
