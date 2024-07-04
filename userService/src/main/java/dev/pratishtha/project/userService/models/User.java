package dev.pratishtha.project.userService.models;

import java.util.HashSet;
import java.util.Set;

public class User extends BaseModel{

    private String email;
    private String username;
    private String password;

    private Set<Role> roles = new HashSet<>();
}
