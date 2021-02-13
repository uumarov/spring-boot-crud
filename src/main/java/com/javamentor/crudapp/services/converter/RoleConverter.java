package com.javamentor.crudapp.services.converter;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements Converter<String, Role> {
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role convert(String roleName) {
        return roleService.findByName(roleName);
    }
}
