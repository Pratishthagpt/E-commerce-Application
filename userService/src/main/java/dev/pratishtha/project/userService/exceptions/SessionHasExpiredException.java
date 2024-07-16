package dev.pratishtha.project.userService.exceptions;

public class SessionHasExpiredException extends RuntimeException {
    public SessionHasExpiredException(String s) {
        super(s);
    }
}
