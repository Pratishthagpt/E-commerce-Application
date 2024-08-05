package dev.pratishtha.project.orderService.exceptions;

public class InsufficientProductQuantityException extends RuntimeException {
    public InsufficientProductQuantityException(String s) {
        super(s);
    }
}
