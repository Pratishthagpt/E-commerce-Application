package dev.pratishtha.project.userService.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequestDto {

    private String email;
    private String username;
    private String password;
}
