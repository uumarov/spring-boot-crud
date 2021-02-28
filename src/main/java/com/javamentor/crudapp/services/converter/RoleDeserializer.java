package com.javamentor.crudapp.services.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class RoleDeserializer extends JsonDeserializer<Set<Role>> {

    private RoleService roleService;

    @Value("${defaultRoleName}")
    private String defaultRoleName;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    public Set<Role> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Set<Role> roles = new HashSet<>();
        if (jsonParser.currentToken() == JsonToken.VALUE_STRING) {
            String roleName = jsonParser.getText();
            roleService.findByName(roleName).ifPresent(roles::add);
        } else if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            jsonParser.nextToken();
            while (jsonParser.hasCurrentToken() && jsonParser.currentToken() != JsonToken.END_ARRAY) {
                String roleName = jsonParser.getValueAsString();
                roleService.findByName(roleName).ifPresent(roles::add);
                jsonParser.nextToken();
            }
        }
        if (roles.isEmpty()) {
            roleService.findByName(defaultRoleName).ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public Set<Role> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return  Set.of(roleService.findByName(defaultRoleName).get());
    }
}
