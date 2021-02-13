package com.javamentor.crudapp.repositories;

import com.javamentor.crudapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(String roleName);
}
