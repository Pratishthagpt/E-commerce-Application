package dev.pratishtha.project.userService.controllers;


import dev.pratishtha.project.userService.dto.RoleDto;
import dev.pratishtha.project.userService.dto.SetUserRolesRequestDto;
import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserDetails (@PathVariable("id") String id) {

        UserDto userDto = userService.getUserDetailsById(id);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("{id}/roles")
    public ResponseEntity<UserDto> setUserRoles (@PathVariable("id") String userId, @RequestBody SetUserRolesRequestDto setUserRolesRequestDto) {
        List<String> roleNames = setUserRolesRequestDto.getRoleNames();
        UserDto userDto = userService.setUserRoles(userId, roleNames);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers () {
        List<UserDto> userDtos = userService.findAllUsers();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}
