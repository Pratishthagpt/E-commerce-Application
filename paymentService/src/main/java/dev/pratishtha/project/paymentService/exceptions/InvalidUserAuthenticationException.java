package dev.pratishtha.project.paymentService.exceptions;

public class InvalidUserAuthenticationException extends RuntimeException {
    public InvalidUserAuthenticationException(String s) {
        super(s);
    }
}
