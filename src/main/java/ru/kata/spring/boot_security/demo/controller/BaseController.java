package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class BaseController {


    private final UserService userService;

    @Autowired
    public BaseController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/registration")
    public String registration (@ModelAttribute("user") User user) {

        return "registration";
    }

    @GetMapping("/main")
    public String mainPage (@ModelAttribute("user") User user) {

        return "main";
    }


    @PostMapping("/registration")
    public String newUser (@ModelAttribute("user") User user, BindingResult bindingResult) {

        userService.saveUser(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/registration";
        }
        return "redirect:/login";
    }

}
