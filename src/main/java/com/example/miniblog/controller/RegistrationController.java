// src/main/java/com/example/miniblog/controller/RegistrationController.java
package com.example.miniblog.controller;

import com.example.miniblog.dto.UserRegistrationDto;
import com.example.miniblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Ключ "user" должен совпадать с th:object="${user}" в template
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") @Valid UserRegistrationDto userDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // Вернём форму обратно с сообщениями об ошибках
            return "registration";
        }
        userService.registerNewUser(userDto);
        return "redirect:/login?registered";
    }
}
