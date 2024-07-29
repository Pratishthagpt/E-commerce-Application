package dev.pratishtha.project.CartService.exceptions;

public class InvalidUserAuthenticationException extends RuntimeException {
    public InvalidUserAuthenticationException(String s) {
        super(s);
    }
}
