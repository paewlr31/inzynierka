package com.example.library.services;

import com.example.library.models.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) { this.bookRepository = bookRepository; }

    public Book addBook(Book book) { return bookRepository.save(book); }

    public List<Book> findAll() { return bookRepository.findAll(); }

    public Optional<Book> findById(Long id) { return bookRepository.findById(id); }

    public void deleteBook(Long id) { bookRepository.deleteById(id); }
}
