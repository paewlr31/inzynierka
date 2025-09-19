package com.example.library.controllers;

import com.example.library.models.BorrowedBook;
import com.example.library.models.User;
import com.example.library.repository.BorrowedRepository;
import com.example.library.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final BorrowedRepository borrowedRepository;
    private final UserRepository userRepository;

    public ProfileController(BorrowedRepository borrowedRepository,
                             UserRepository userRepository) {
        this.borrowedRepository = borrowedRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/borrowed")
    public List<Map<String, Object>> getBorrowedBooks(HttpSession session) {
        // Pobranie userId z sesji
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("Nie zalogowany");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        List<BorrowedBook> borrowedList = borrowedRepository.findByUser(user);

        // Mapowanie na listę "książek" do JSON
        return borrowedList.stream().map(borrowedBook -> {
            var book = borrowedBook.getBook();
            Map<String, Object> map = new HashMap<>();
            map.put("id", book.getId());
            map.put("title", book.getTitle());
            map.put("author", book.getAuthor());
            map.put("genre", book.getGenre().name());
            map.put("image", book.getImage() != null ? Base64.getEncoder().encodeToString(book.getImage()) : null);
            map.put("borrowedAt", borrowedBook.getBorrowedAt().toString()); // czas wypożyczenia
            map.put("dueAt", borrowedBook.getBorrowedAt().plusDays(60).toString()); // termin oddania po 60 dniach
            return map;
        }).collect(Collectors.toList());
    }
}
