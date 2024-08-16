package dev.pratishtha.project.userService.controllers;

import dev.pratishtha.project.userService.dtos.CreateRoleRequestDto;
import dev.pratishtha.project.userService.dtos.ExceptionDto;
import dev.pratishtha.project.userService.dtos.RoleDto;
import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.services.RoleService;
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
@RequestMapping("/users/roles")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Create new roles for user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role has successfully added.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))
                    }),
            @ApiResponse(responseCode = "208", description = "Role already present.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))
                    })
    })
    @PostMapping()
    public ResponseEntity<RoleDto> createNewRole (@RequestBody CreateRoleRequestDto createRoleRequestDto) {
        RoleDto roleDto = roleService.createNewRole(createRoleRequestDto.getRoleName());

        return new ResponseEntity<>(roleDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SUCCESS.",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Role.class))
            })
    })
    @GetMapping()
    public ResponseEntity<List<RoleDto>> getAllRoles () {
         List<RoleDto> roles = roleService.findAllRoles();

         return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
