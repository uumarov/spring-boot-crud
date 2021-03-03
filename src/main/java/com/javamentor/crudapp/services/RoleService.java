package com.javamentor.crudapp.services;

import com.javamentor.crudapp.entities.Role;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    public Set<Role> findAll();

    public Optional<Role> findByName(String roleName);

    public Set<Role> findAllByNameIn(Collection<String> roleNames);
}
