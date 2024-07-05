package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dto.CreateRoleRequestDto;
import dev.pratishtha.project.userService.dto.RoleDto;
import dev.pratishtha.project.userService.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<List<RoleDto>> getAllRoles () {
         List<RoleDto> roles = roleService.findAllRoles();

         return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
