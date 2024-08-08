package dev.pratishtha.project.orderService.security;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JwtData {

    private String userId;
    private String email;
    private String username;
    private String phoneNo;

    private List<UserRole> roles;
}
