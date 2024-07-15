package dev.pratishtha.project.productService.exceptions;

public class InvalidUserAuthenticationException extends RuntimeException{
    public InvalidUserAuthenticationException(String message) {
        super(message);
    }
}
