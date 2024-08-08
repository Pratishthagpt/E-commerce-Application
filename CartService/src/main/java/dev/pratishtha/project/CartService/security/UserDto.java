package dev.pratishtha.project.CartService.security;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private String userId;
    private String email;
    private String username;
    private String phoneNo;

    private Set<UserRole> roles = new HashSet<>();
}
