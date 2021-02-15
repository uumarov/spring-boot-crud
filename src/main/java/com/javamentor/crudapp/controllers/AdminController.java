package com.javamentor.crudapp.controllers;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.entities.User;
import com.javamentor.crudapp.services.RoleService;
import com.javamentor.crudapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping(value = "/edit/{id}")
    public String userEditForm(Model model, @PathVariable(name = "id") Long id) {
        User user = userService.findOneById(id);
        List<Role> allRoles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "user-form";
    }

    @GetMapping(value = "/add")
    public String userAddForm(Model model) {
        User user = new User();
        List<Role> allRoles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "user-form";
    }

    @PostMapping(value = "/add")
    public String userCreate(@ModelAttribute(name = "user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/edit")
    public String userSave(@ModelAttribute(name = "user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String userAddForm(@PathVariable(name = "id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
