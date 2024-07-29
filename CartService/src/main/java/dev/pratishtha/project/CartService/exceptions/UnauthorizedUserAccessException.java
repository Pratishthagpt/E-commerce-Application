package dev.pratishtha.project.CartService.exceptions;

public class UnauthorizedUserAccessException extends RuntimeException {
    public UnauthorizedUserAccessException(String s) {
        super(s);
    }
}
