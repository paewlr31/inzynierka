package com.example.library.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.library.models.Book;
import com.example.library.models.BorrowedBook;
import com.example.library.models.User;
import com.example.library.repository.UserRepository;
import com.example.library.services.BookService;
import com.example.library.services.BorrowService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BorrowController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final UserRepository userRepository;

    public BorrowController(BookService bookService, BorrowService borrowService, UserRepository userRepository) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.userRepository = userRepository;
    }

    @PostMapping("/books/borrow/{id}")
    public String borrowBook(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Optional<User> userOpt = userRepository.findById(userId);  // <-- tutaj repo
        Optional<Book> bookOpt = bookService.findById(id);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                bookService.addBook(book);

                borrowService.borrowBook(new BorrowedBook(userOpt.get(), book));
            }
        }
        return "redirect:/profile";
    }
}


