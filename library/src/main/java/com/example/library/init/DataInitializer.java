package com.example.library.init;

import com.example.library.models.User;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // sprawdzamy czy admin już istnieje
        if (userRepository.findByUserName("admin").isEmpty()) {
            User admin = new User();
            admin.setUserName("admin");
            admin.setDisplayName("Administrator");
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            // hasło startowe
            admin.setPasswordHash(BCrypt.hashpw("Admin123!", BCrypt.gensalt()));
            userRepository.save(admin);
            System.out.println("Admin został utworzony: login='admin', hasło='Admin123!'");
        }
    }
}
