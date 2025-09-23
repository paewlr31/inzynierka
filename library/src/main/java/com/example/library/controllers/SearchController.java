package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.models.Genre;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SearchController {

    private final BookRepository bookRepository;

    public SearchController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String searchBooks(@RequestParam(value = "filter", required = false) String filter,
                              @RequestParam(value = "query", required = false) String query,
                              @RequestParam(value = "sort", required = false) String sort,
                              Model model) {

        List<Book> books;

        if ("alpha".equals(sort)) {
            books = bookRepository.findAllByOrderByTitleAsc();
        } else if ("newest".equals(sort)) {
            books = bookRepository.findTop15ByOrderByCreatedAtDesc();
        } else if (query != null && !query.isBlank()) {
            if ("author".equals(filter)) {
                books = bookRepository.findByAuthorContainingIgnoreCase(query);
            } else if ("genre".equals(filter)) {
                try {
                    Genre genreEnum = Genre.valueOf(query.toUpperCase().replace(" ", "_"));
                    books = bookRepository.findByGenre(genreEnum);
                } catch (IllegalArgumentException e) {
                    books = Collections.emptyList();
                }
            }
 else if ("year".equals(filter)) {
                try {
                    Integer year = Integer.valueOf(query);
                    books = bookRepository.findByYear(year);
                } catch (NumberFormatException e) {
                    books = Collections.emptyList();
                }
            } else {
                // domyślnie szukanie po tytule
                books = bookRepository.findByTitleContainingIgnoreCase(query);
            }
        } else {
            // brak zapytania → domyślnie 15 ostatnich
            books = bookRepository.findTop15ByOrderByCreatedAtDesc();
        }

        model.addAttribute("books", books);
        model.addAttribute("imagesBase64", convertImages(books));
        return "index"; // wyniki renderujemy na stronie głównej
    }

    private Map<Long, String> convertImages(List<Book> books) {
        Map<Long, String> images = new HashMap<>();
        for (Book b : books) {
            if (b.getImage() != null) {
                images.put(b.getId(), Base64.getEncoder().encodeToString(b.getImage()));
            }
        }
        return images;
    }
}
