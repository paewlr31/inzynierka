package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.models.BorrowedBook;
import com.example.library.repository.BorrowedRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Controller
@RequestMapping("/books")
public class BorrowedBookController {

    private final BorrowedRepository borrowedRepository;

    public BorrowedBookController(BorrowedRepository borrowedRepository) {
        this.borrowedRepository = borrowedRepository;
    }

    @GetMapping("/borrowed/{borrowId}")
    public String borrowedBookDetails(@PathVariable Long borrowId, Model model) {
        BorrowedBook borrowed = borrowedRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wypożyczenia o ID: " + borrowId));

        Book book = borrowed.getBook();
        String imageBase64 = null;
        if (book.getImage() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(book.getImage());
        }

        model.addAttribute("book", book);
        model.addAttribute("borrowId", borrowed.getId());
        model.addAttribute("imageBase64", imageBase64);
        model.addAttribute("borrowedAt", borrowed.getBorrowedAt()); // data wypożyczenia
        model.addAttribute("dueAt", borrowed.getBorrowedAt().plusDays(60)); // termin oddania

        return "borrowed-book-details"; // Twój HTML z przyciskiem Przedłuż
    }
}
