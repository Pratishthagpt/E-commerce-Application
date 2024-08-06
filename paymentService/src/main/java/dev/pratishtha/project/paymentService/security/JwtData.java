package dev.pratishtha.project.paymentService.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtData {

    private String userId;
    private String email;
    private String username;
    private List<UserRole> roles;
}
