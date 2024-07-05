package dev.pratishtha.project.userService.controllers;


import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
