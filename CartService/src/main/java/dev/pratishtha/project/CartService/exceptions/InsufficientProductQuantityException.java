package dev.pratishtha.project.CartService.exceptions;

public class InsufficientProductQuantityException extends RuntimeException {
    public InsufficientProductQuantityException(String s) {
        super(s);
    }
}
