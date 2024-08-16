package dev.pratishtha.project.userService.controllers;


import dev.pratishtha.project.userService.dtos.ExceptionDto;
import dev.pratishtha.project.userService.dtos.SetUserRolesRequestDto;
import dev.pratishtha.project.userService.dtos.UserDto;
import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get User by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "User does not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserDetails (@PathVariable("id") String id) {

        UserDto userDto = userService.getUserDetailsById(id);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Set User roles by user Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "User does not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping("{id}/roles")
    public ResponseEntity<UserDto> setUserRoles (@PathVariable("id") String userId, @RequestBody SetUserRolesRequestDto setUserRolesRequestDto) {
        List<String> roleNames = setUserRolesRequestDto.getRoleNames();
        UserDto userDto = userService.setUserRoles(userId, roleNames);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Get all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
                    })
    })
    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers () {
        List<UserDto> userDtos = userService.findAllUsers();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
}
