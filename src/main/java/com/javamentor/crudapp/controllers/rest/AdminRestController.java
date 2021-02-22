package com.javamentor.crudapp.controllers.rest;

import com.javamentor.crudapp.entities.User;
import com.javamentor.crudapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> all() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public User newUser(@RequestBody User newUser) {
        return userService.save(newUser);
    }

    @GetMapping("/users/{id}")
    public User one(@PathVariable Long id) {
        return userService.findOneById(id).get();
    }

    @DeleteMapping
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
