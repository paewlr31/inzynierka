package com.example.library.repository;

import com.example.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Tutaj możesz dodać dodatkowe metody, np. wyszukiwanie po tytule lub autorze
    // List<Book> findByTitleContainingIgnoreCase(String title);
}
