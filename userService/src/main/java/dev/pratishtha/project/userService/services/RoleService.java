package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dto.RoleDto;
import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDto createNewRole(String roleName) {
        Role role = new Role();
        role.setRole(roleName);

        Role savedRole = roleRepository.save(role);

        RoleDto roleDto = new RoleDto();
        roleDto.setRole(savedRole);

        return roleDto;
    }
}
