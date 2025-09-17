package com.example.library;

import com.example.library.models.Book;
import com.example.library.models.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public TestDataLoader(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("haslo123");
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Java dla początkujących");
        book.setUser(user);
        bookRepository.save(book);

        System.out.println("Testowe dane zapisane do bazy!");
    }
}
