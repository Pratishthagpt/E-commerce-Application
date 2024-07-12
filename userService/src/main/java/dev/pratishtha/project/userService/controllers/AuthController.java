package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dtos.*;
import dev.pratishtha.project.userService.models.Session;
import dev.pratishtha.project.userService.models.SessionStatus;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.services.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/users/auth")
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

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser (@RequestBody LoginRequestDto loginRequestDto) {
//        log("Request received from body.")    // logging every request

        User user = authService.loginUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        UserDto userDto = UserDto.fromUser(user);
        userDto.setRoles(user.getRoles());

        String token = RandomStringUtils.randomAlphanumeric(30);
//        creating session
        Session session = authService.createSession(token, user);

//    setting token and cookie in headers
        MultiValueMap<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token: " + token);

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto> logoutUser (@RequestBody LogoutRequestDto logoutRequestDto) {
        authService.logoutUser(logoutRequestDto.getUserId(), logoutRequestDto.getToken());

        SuccessResponseDto successResponseDto = new SuccessResponseDto("User has successfully logout.", HttpStatus.OK);
        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken (@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        SessionStatus sessionStatus = authService.validateUserToken(validateTokenRequestDto.getUserId(), validateTokenRequestDto.getToken());

        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
}
