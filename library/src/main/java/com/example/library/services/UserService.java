package com.example.library.services;

import com.example.library.models.User;
import com.example.library.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    // minimalne zabezpieczenie: BCrypt (bez Spring Security config)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User register(String userName, String plainPassword, String email, String displayName) throws IllegalArgumentException {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new IllegalArgumentException("Nazwa użytkownika zajęta");
        }
        if (email != null && !email.isBlank() && userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail już użyty");
        }

        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User u = new User();
        u.setUserName(userName);
        u.setPasswordHash(hash);
        u.setEmail(email);
        u.setDisplayName(displayName == null || displayName.isBlank() ? userName : displayName);
        u.setRole("USER");
        return userRepository.save(u);
    }

    public Optional<User> authenticate(String userNameOrEmail, String plainPassword) {
        Optional<User> byName = userRepository.findByUserName(userNameOrEmail);
        Optional<User> byEmail = userRepository.findByEmail(userNameOrEmail);
        Optional<User> candidate = byName.isPresent() ? byName : byEmail;

        if (candidate.isPresent()) {
            User u = candidate.get();
            if (BCrypt.checkpw(plainPassword, u.getPasswordHash())) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public void changePassword(User user, String newPlain) {
        String hash = BCrypt.hashpw(newPlain, BCrypt.gensalt());
        user.setPasswordHash(hash);
        userRepository.save(user);
    }

  
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    
   


	
}

