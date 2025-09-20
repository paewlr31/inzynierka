package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.models.BorrowedBook;
import com.example.library.repository.BorrowedRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.Optional;

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
        model.addAttribute("dueAt", borrowed.getDueAt()); // faktyczne dueAt z encji

        model.addAttribute("extensionUsed", borrowed.isExtensionUsed());

        return "borrowed-book-details"; // Twój HTML z przyciskiem Przedłuż
    }
    

    @PostMapping("/extend/{borrowId}")
    public String extendBook(@PathVariable Long borrowId, RedirectAttributes redirectAttributes) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedRepository.findById(borrowId);

        if (borrowedBookOpt.isPresent()) {
            BorrowedBook borrowedBook = borrowedBookOpt.get();

            if (borrowedBook.isExtensionUsed()) {
                redirectAttributes.addFlashAttribute("error", "Nie możesz przedłużyć tej książki więcej niż raz.");
            } else {
                // przedłużenie o 7 dni od obecnego dueAt
                borrowedBook.setDueAt(borrowedBook.getDueAt().plusDays(60));
                borrowedBook.setExtensionUsed(true);
                borrowedRepository.save(borrowedBook);
                redirectAttributes.addFlashAttribute("success", "Wypożyczenie zostało przedłużone o 7 dni.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie znaleziono wypożyczenia o podanym ID.");
        }

        return "redirect:/books/borrowed/" + borrowId;
    }

}
