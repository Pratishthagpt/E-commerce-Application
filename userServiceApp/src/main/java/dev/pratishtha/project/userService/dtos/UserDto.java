package dev.pratishtha.project.userService.dtos;

import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private String userId;
    private String email;
    private String username;

    private Set<Role> roles = new HashSet<>();

    public static UserDto fromUser (User user) {
        UserDto userDto = new UserDto();

        userDto.setUserId(String.valueOf(user.getUuid()));
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());

        return userDto;
    }
}
