package com.example.library.repository;

import com.example.library.models.Book;
import com.example.library.models.Genre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Tutaj możesz dodać dodatkowe metody, np. wyszukiwanie po tytule lub autorze
    // List<Book> findByTitleContainingIgnoreCase(String title);
	List<Book> findAllByOrderByCreatedAtAsc();   // najstarsze pierwsze
    List<Book> findAllByOrderByCreatedAtDesc();  // najnowsze pierwsze
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenre(Genre genre);

    List<Book> findByYear(Integer year);

    List<Book> findAllByOrderByTitleAsc();
    List<Book> findTop15ByOrderByCreatedAtDesc();
}
