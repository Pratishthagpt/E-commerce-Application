package dev.pratishtha.project.userService.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequestDto {

    private String email;
    private String username;
    private String password;
}
