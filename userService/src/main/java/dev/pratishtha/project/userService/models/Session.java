package dev.pratishtha.project.userService.models;

import java.util.Date;

public class Session extends BaseModel{

    private String token;
    private Date expiryAt;

    private SessionStatus sessionStatus;
    private User user;
}
