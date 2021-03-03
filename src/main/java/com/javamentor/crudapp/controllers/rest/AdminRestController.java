package com.javamentor.crudapp.controllers.rest;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.entities.User;
import com.javamentor.crudapp.services.RoleService;
import com.javamentor.crudapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> all() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> newUser(@RequestBody User newUser) {
        User user = userService.save(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/users/edit/{id}")
    public ResponseEntity<User> editUser(@RequestBody User newUser, @PathVariable Long id) {
        System.out.println(userService.updateWithQuery(newUser, id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> one(@PathVariable Long id) {
        Optional<User> userOptional = userService.findOneById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> allRoles() {
        Set<Role> allRoles = roleService.findAll();
        return new ResponseEntity<>(allRoles, HttpStatus.OK);
    }


}
