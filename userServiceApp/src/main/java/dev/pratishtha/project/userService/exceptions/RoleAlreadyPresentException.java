package dev.pratishtha.project.userService.exceptions;


public class RoleAlreadyPresentException extends RuntimeException{
    public RoleAlreadyPresentException(String message) {
        super(message);
    }
}
