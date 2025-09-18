package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.services.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        List<Book> books = bookService.findAllByCreatedAtDesc();

        Map<Long, String> imagesBase64 = new HashMap<>();
        for (Book book : books) {
            if (book.getImage() != null) {
                imagesBase64.put(book.getId(), Base64.getEncoder().encodeToString(book.getImage()));
            }
        }

        model.addAttribute("books", books);
        model.addAttribute("imagesBase64", imagesBase64);
        return "books";
    }
    
    @GetMapping("/books/details/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono książki o ID: " + id));

        String imageBase64 = null;
        if (book.getImage() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(book.getImage());
        }

        model.addAttribute("book", book);
        model.addAttribute("imageBase64", imageBase64);
        return "book-details"; // nazwa Twojego szablonu .html
    }

}

