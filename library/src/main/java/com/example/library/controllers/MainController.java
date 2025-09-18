package com.example.library.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Collections;


@Controller
public class MainController {


@GetMapping("/")
public String index() {
return "index"; // src/main/resources/templates/index.html
}


@GetMapping("/books")
public String books(Model model) {
// na start pusty list (później podmienisz na repozytorium z DB)
model.addAttribute("books", Collections.emptyList());
return "books";
}


/*
 * @GetMapping("/myprofile") public String profile() { return "profile"; }
 */


@GetMapping("/about")
public String about() {
return "about";
}


@GetMapping("/contact")
public String contact() {
return "contact";
}


@GetMapping("/admin")
public String admin() {
return "admin";
}

/*
 * @GetMapping("/login") public String login() { return "login"; }
 */


/*
 * @GetMapping("/register") public String register() { return "register"; }
 */
}