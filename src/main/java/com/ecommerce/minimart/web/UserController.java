package com.ecommerce.minimart.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.ecommerce.minimart.domain.User;
import com.ecommerce.minimart.domain.UserRepository;
import jakarta.validation.Valid;



@Controller
public class UserController {
    @Autowired
    UserRepository urepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user, Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid User user, BindingResult bindingResult, String passwordString) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        user.setRole("USER");
        user.setPasswordHash(new BCryptPasswordEncoder().encode(user.getPassword()));

        urepository.save(user);

        return "redirect:/login";
    }

}
