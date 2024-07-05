package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dto.SignUpRequestDto;
import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUpUser (@RequestBody SignUpRequestDto signUpRequestDto) {
        UserDto userDto = authService.signUpUser(signUpRequestDto.getEmail(), signUpRequestDto.getUsername(), signUpRequestDto.getPassword());

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
