package com.javamentor.crudapp.controllers;

import com.javamentor.crudapp.entities.User;
import com.javamentor.crudapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal) {
        User currentUser = (principal != null)
                ? (User) userService.loadUserByUsername(principal.getName())
                : new User();
        model.addAttribute("currentUser", currentUser);
        return "main";
    }

}
