package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.models.User;
import com.example.library.services.BookService;
import com.example.library.services.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Base64;

@Controller
public class AdminController {
	private final UserService userService;
    private final BookService bookService;
    public AdminController(BookService bookService, UserService userService) { this.userService = userService;
	this.bookService = bookService; }
    
    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        List<Book> books = bookService. findAllByCreatedAtDesc();

        // przygotowanie Base64 dla każdego obrazka
        Map<Long, String> imagesBase64 = new HashMap<>();
        for (Book book : books) {
            if (book.getImage() != null) {
                imagesBase64.put(book.getId(), Base64.getEncoder().encodeToString(book.getImage()));
            }
        }

        model.addAttribute("books", books);
        model.addAttribute("imagesBase64", imagesBase64);

        return "admin";
    }


    @GetMapping("/admin/add-book")
    public String addBookForm(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";
        return "add-book";
    }

    @PostMapping("/admin/add-book")
    public String addBookSubmit(@RequestParam String title,
                                @RequestParam String author,
                                @RequestParam int quantity,
                                @RequestParam String description,
                                @RequestParam(required = false) Integer year,
                                @RequestParam("image") MultipartFile image,
                                HttpSession session) throws IOException {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setQuantity(quantity);
        b.setDescription(description);
        b.setYear(year);

        // zapis obrazka w bazie
        if (!image.isEmpty()) {
            b.setImage(image.getBytes());
        }

        bookService.addBook(b);
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/edit-book/{id}")
    public String editBookForm(@PathVariable Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        Optional<Book> bookOpt = bookService.findById(id);
        if (bookOpt.isEmpty()) return "redirect:/admin";

        Book book = bookOpt.get();
        model.addAttribute("book", book);

        if (book.getImage() != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(book.getImage());
            model.addAttribute("imageBase64", imageBase64);
        }

        return "edit-book";
    }
    

    @PostMapping("/admin/edit-book")
    public String editBookSubmit(@RequestParam Long id,
                                @RequestParam String title,
                                @RequestParam String author,
                                @RequestParam int quantity,
                                @RequestParam String description,
                                @RequestParam(required = false) Integer year,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                HttpSession session) throws IOException {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        // Znajdź istniejącą książkę
        Optional<Book> bookOpt = bookService.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            
            // Aktualizuj pola
            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);
            book.setDescription(description);
            book.setYear(year);
            
            // Aktualizuj obrazek TYLKO jeśli przesłano nowy
            if (imageFile != null && !imageFile.isEmpty()) {
                book.setImage(imageFile.getBytes());
            }
            
            bookService.addBook(book);
        }
        
        return "redirect:/admin";
    }



	
 // Usuwanie książki - wersja z POST (bardziej bezpieczna)
    @PostMapping("/admin/delete-book/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";
        
        bookService.deleteBook(id);
        return "redirect:/admin";
    }
    
    
    @GetMapping("/admin/delete-book/{id}")
    public String confirmDeleteBook(@PathVariable Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        Optional<Book> bookOpt = bookService.findById(id);
        if (bookOpt.isEmpty()) return "redirect:/admin";

        Book book = bookOpt.get();
        model.addAttribute("book", book);

        // dodajemy obrazek w Base64
        if (book.getImage() != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(book.getImage());
            model.addAttribute("imageBase64", imageBase64);
        }

        return "delete-book";
    }



    @GetMapping("/books/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        bookService.findById(id).ifPresent(book -> model.addAttribute("book", book));
        return "book-details";
    }
    
    @GetMapping("/admin/users")
    public String listUsers(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        List<User> users = userService.findAll(); // potrzebujesz metodę findAll() w UserService
        model.addAttribute("users", users);

        return "admin-users";
    }
    
    @GetMapping("/admin/delete-user/{id}")
    public String confirmDeleteUser(@PathVariable Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        userService.findById(id).ifPresent(user -> model.addAttribute("user", user));
        return "delete-user"; // nowy widok do potwierdzenia
    }

    @PostMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) return "redirect:/";

        userService.deleteById(id); // metoda do zaimplementowania w UserService
        return "redirect:/admin/users";
    }



    
    
}

