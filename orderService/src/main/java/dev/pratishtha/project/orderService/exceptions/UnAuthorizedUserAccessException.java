package dev.pratishtha.project.orderService.exceptions;

public class UnAuthorizedUserAccessException extends RuntimeException {
    public UnAuthorizedUserAccessException(String s) {
        super(s);
    }
}
