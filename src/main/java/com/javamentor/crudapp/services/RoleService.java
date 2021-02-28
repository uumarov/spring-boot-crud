package com.javamentor.crudapp.services;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> findAll() {
        return new HashSet<>(roleRepository.findAll());
    }

    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByRole(roleName);
    }

    public Set<Role> findAllByNameIn(Collection<String> roleNames) {
        return roleRepository.findAllByRoleIn(roleNames);
    }
}
