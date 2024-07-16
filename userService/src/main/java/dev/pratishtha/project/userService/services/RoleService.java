package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dtos.RoleDto;
import dev.pratishtha.project.userService.exceptions.RoleAlreadyPresentException;
import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDto createNewRole(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByRole(roleName);
        if (roleOptional.isPresent()) {
            throw new RoleAlreadyPresentException("Role - " + roleName + " already present. Duplicate not allowed.");
        }

        Role role = new Role();
        role.setRole(roleName);

        Role savedRole = roleRepository.save(role);

        RoleDto roleDto = new RoleDto();
        roleDto.setRole(savedRole);

        return roleDto;
    }

    public List<RoleDto> findAllRoles() {
        List<Role> roleList = roleRepository.findAll();

        List<RoleDto> roleDtosList = new ArrayList<>();

        for (Role role : roleList) {
            RoleDto roleDto = new RoleDto();
            roleDto.setRole(role);
            roleDtosList.add(roleDto);
        }
        return roleDtosList;
    }
}
