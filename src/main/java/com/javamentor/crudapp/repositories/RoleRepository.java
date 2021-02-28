package com.javamentor.crudapp.repositories;

import com.javamentor.crudapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(String roleName);
    Set<Role> findAllByRoleIn(Collection<String> roleNames);
    Optional<Role> findByRole(String roleName);
}
