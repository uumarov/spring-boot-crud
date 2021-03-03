package com.javamentor.crudapp.services;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.entities.User;
import com.javamentor.crudapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<User> findOneById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User newUser, Long id) {
        return findOneById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setAge(newUser.getAge());
                    user.setRoles(newUser.getRoles());
                    user.setEmail(newUser.getEmail());
                    if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
                        user.setPassword(newUser.getPassword());
                        save(user);
                    }
                    return update(user);
                })
                .orElseGet(() -> save(newUser));
    }

    @Transactional
    public int updateWithQuery(User newUser, Long id) {
        String password = (newUser.getPassword() != null && !newUser.getPassword().isBlank())
                ? passwordEncoder.encode(newUser.getPassword())
                : null;
        System.out.println(password);
        userRepository.update(newUser, password, id);
        userRepository.updateUserRoles(newUser.getRoles(), id);
        return 1;
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }
}
