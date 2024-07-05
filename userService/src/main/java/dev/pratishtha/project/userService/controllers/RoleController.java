package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dto.CreateRoleRequestDto;
import dev.pratishtha.project.userService.dto.RoleDto;
import dev.pratishtha.project.userService.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/roles")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    public ResponseEntity<RoleDto> createNewRole (@RequestBody CreateRoleRequestDto createRoleRequestDto) {
        RoleDto roleDto = roleService.createNewRole(createRoleRequestDto.getRoleName());

        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }


}
