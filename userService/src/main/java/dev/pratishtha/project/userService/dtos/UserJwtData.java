package dev.pratishtha.project.userService.dtos;

import dev.pratishtha.project.userService.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserJwtData {
    private String email;
    private String username;
    private Set<Role> roleList;

}
