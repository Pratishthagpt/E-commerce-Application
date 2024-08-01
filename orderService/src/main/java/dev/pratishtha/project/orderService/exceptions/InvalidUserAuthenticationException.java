package dev.pratishtha.project.orderService.exceptions;

public class InvalidUserAuthenticationException extends RuntimeException {
    public InvalidUserAuthenticationException(String s) {
        super(s);
    }
}
