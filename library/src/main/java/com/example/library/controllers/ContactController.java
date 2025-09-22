package com.example.library.controllers;

import com.example.library.models.Message;
import com.example.library.models.User;
import com.example.library.repository.MessageRepository;
import com.example.library.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ContactController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public String contactPage(@RequestParam(required = false) Long userId, HttpSession session, Model model) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (sessionUserId == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("role", role);

        if ("ADMIN".equals(role)) {
            List<User> users = userRepository.findAll().stream()
                    .filter(u -> !"ADMIN".equals(u.getRole()))
                    .toList();
            model.addAttribute("users", users);

            if (userId != null) {
                Optional<User> selectedUserOpt = userRepository.findById(userId);
                if (selectedUserOpt.isEmpty()) {
                    model.addAttribute("error", "Selected user not found");
                    return "contact";
                }
                User selectedUser = selectedUserOpt.get();
                List<Message> messages = messageRepository.findConversation(currentUser, selectedUser);
                model.addAttribute("selectedUser", selectedUser);
                model.addAttribute("messages", messages);
            }
        } else {
            Optional<User> adminOpt = userRepository.findAll().stream()
                    .filter(u -> "ADMIN".equals(u.getRole()))
                    .findFirst();
            if (adminOpt.isEmpty()) {
                model.addAttribute("error", "No admin user found");
                return "contact";
            }
            User admin = adminOpt.get();
            List<Message> messages = messageRepository.findConversation(currentUser, admin);
            model.addAttribute("messages", messages);
            model.addAttribute("admin", admin);
        }

        return "contact";
    }

    @PostMapping("/send")
    public String sendMessage(HttpSession session, @RequestParam Long receiverId, @RequestParam String content) {
        Long senderId = (Long) session.getAttribute("userId");
        if (senderId == null) {
            return "redirect:/login";
        }
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/contact?error=empty_message";
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content.trim());
        messageRepository.save(message);

        if ("ADMIN".equals(sender.getRole())) {
            return "redirect:/contact?userId=" + receiverId;
        } else {
            return "redirect:/contact";
        }
    }

    @GetMapping("/messages/{userId}")
    @ResponseBody
    public List<MessageDTO> getMessages(@PathVariable Long userId, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            throw new RuntimeException("Nie zalogowany");
        }

        User currentUser = userRepository.findById(sessionUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));

        List<Message> messages = messageRepository.findConversation(currentUser, otherUser);

        return messages.stream().map(m -> new MessageDTO(
                m.getSender().getDisplayName(),
                m.getContent(),
                m.getSender().getRole()
        )).toList();
    }

    public static class MessageDTO {
        private String sender;
        private String content;
        private String role;

        public MessageDTO(String sender, String content, String role) {
            this.sender = sender;
            this.content = content;
            this.role = role;
        }

        public String getSender() { return sender; }
        public String getContent() { return content; }
        public String getRole() { return role; }
    }
}