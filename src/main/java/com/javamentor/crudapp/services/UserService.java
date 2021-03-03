package com.javamentor.crudapp.services;

import com.javamentor.crudapp.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public Optional<User> findOneById(Long id);

    public List<User> findAll();

    public User save(User user);

    public User update(User newUser, Long id);

    public void delete(User user);

    public void delete(Long id);

    public Optional<User> findUserByUsername(String username);
}
