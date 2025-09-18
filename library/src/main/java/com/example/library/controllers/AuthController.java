package com.example.library.controllers;

import com.example.library.models.User;
import com.example.library.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // strony
    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @GetMapping("/profile")
    public String profilePage() { return "profile"; }

    // REST API endpoints (JSON) dla AJAX
    @PostMapping("/api/login")
    @ResponseBody
    public Map<String,Object> apiLogin(@RequestBody Map<String,String> body, HttpSession session) {
        String user = body.get("user");
        String pass = body.get("password");
        if (user == null || pass == null) return Map.of("ok", false, "error", "Brak danych");

        Optional<User> u = userService.authenticate(user, pass);
        if (u.isPresent()) {
            // zapisz tylko minimalne info do sesji
            session.setAttribute("userId", u.get().getId());
            session.setAttribute("userName", u.get().getUserName());
            session.setAttribute("displayName", u.get().getDisplayName());
            session.setAttribute("role", u.get().getRole());
            return Map.of("ok", true, "userName", u.get().getUserName(), "displayName", u.get().getDisplayName());
        } else {
            return Map.of("ok", false, "error", "Nieprawidłowe dane logowania");
        }
    }

    @PostMapping("/api/logout")
    @ResponseBody
    public Map<String,Object> apiLogout(HttpSession session) {
        session.invalidate();
        return Map.of("ok", true);
    }

    @PostMapping("/api/register")
    @ResponseBody
    public Map<String,Object> apiRegister(@RequestBody Map<String,String> body, HttpSession session) {
        String user = body.get("user");
        String pass = body.get("password");
        String email = body.get("email");
        String display = body.get("displayName");

        if (user == null || pass == null || email == null || display == null)
            return Map.of("ok", false, "error", "Brak danych");

        if (pass.length() < 7) 
            return Map.of("ok", false, "error", "Hasło musi mieć przynajmniej 7 znaków");

        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))
            return Map.of("ok", false, "error", "Niepoprawny adres email");

        try {
            User newU = userService.register(user, pass, email, display);
            // automatyczne zalogowanie po rejestracji:
            session.setAttribute("userId", newU.getId());
            session.setAttribute("userName", newU.getUserName());
            session.setAttribute("displayName", newU.getDisplayName());
            session.setAttribute("role", newU.getRole());
            return Map.of("ok", true, "userName", newU.getUserName());
        } catch (IllegalArgumentException ex) {
            return Map.of("ok", false, "error", ex.getMessage());
        }
    }
    
    
    @PostMapping("/api/reset-password")
    @ResponseBody
    public Map<String,Object> apiResetPassword(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String newPass = body.get("newPassword");
        String confirm = body.get("confirmPassword");

        if (email == null || newPass == null || confirm == null)
            return Map.of("ok", false, "error", "Brak danych");

        if (!newPass.equals(confirm))
            return Map.of("ok", false, "error", "Nowe hasła nie są zgodne");

        if (newPass.length() < 7)
            return Map.of("ok", false, "error", "Hasło musi mieć przynajmniej 7 znaków");

        Optional<User> uOpt = userService.findByEmail(email);
        if (uOpt.isEmpty())
            return Map.of("ok", false, "error", "Nie ma użytkownika z takim e-mailem");

        userService.changePassword(uOpt.get(), newPass);
        return Map.of("ok", true, "message", "Hasło zostało zmienione. Zaloguj się nowym hasłem.");
    }




    // endpoint do sprawdzania sesji (używane przez JS)
    @GetMapping("/api/session")
    @ResponseBody
    public Map<String,Object> apiSession(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return Map.of("logged", false);
        return Map.of(
                "logged", true,
                "userId", userId,
                "userName", session.getAttribute("userName"),
                "displayName", session.getAttribute("displayName"),
                "role", session.getAttribute("role")
        );
    }
}
