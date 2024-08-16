package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dtos.*;
import dev.pratishtha.project.userService.models.Session;
import dev.pratishtha.project.userService.models.SessionStatus;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Enter user details for signing up.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully signed up.",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
            })
    })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUpUser (@RequestBody SignUpRequestDto signUpRequestDto) {
        UserDto userDto = authService.signUpUser(signUpRequestDto.getEmail(), signUpRequestDto.getUsername()
                , signUpRequestDto.getPassword(), signUpRequestDto.getPhoneNo());

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Enter user details for login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has successfully login.",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request. User has entered incorrect details.",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "User does not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser (@RequestBody LoginRequestDto loginRequestDto) {
//        log("Request received from body.")    // logging every request

        User user = authService.loginUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        UserDto userDto = UserDto.fromUser(user);
        userDto.setRoles(user.getRoles());

//        creating jwt token instead of putting random alphanumeric string
        String token = authService.generateJwtToken(user);
//        creating session
        Session session = authService.createSession(token, user);

//    setting token and cookie in headers
        MultiValueMap<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token: " + token);

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    @Operation(summary = "Logging user out.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has successfully logout.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "User does not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto> logoutUser (@RequestBody LogoutRequestDto logoutRequestDto) {
        authService.logoutUser(logoutRequestDto.getUserId(), logoutRequestDto.getToken());

        SuccessResponseDto successResponseDto = new SuccessResponseDto("User has successfully logout.", HttpStatus.OK);
        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Validating user using JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is valid.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Token is invalid / Session is expired.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "User does not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping("/validate")
    public ResponseEntity<UserDto> validateToken (@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {

        UserJwtData userJwtData = authService.validateUserToken(validateTokenRequestDto.getToken());

        UserDto userDto = new UserDto();
        userDto.setUserId(userJwtData.getId());
        userDto.setEmail(userJwtData.getEmail());
        userDto.setUsername(userJwtData.getUsername());
        userDto.setRoles(userJwtData.getRoleList());
        userDto.setPhoneNo(userJwtData.getPhoneNo());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
