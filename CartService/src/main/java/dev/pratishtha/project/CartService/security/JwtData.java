package dev.pratishtha.project.CartService.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtData {

    private String userId;
    private String email;
    private String username;
    private String phoneNo;

    private List<UserRole> roles;
}
