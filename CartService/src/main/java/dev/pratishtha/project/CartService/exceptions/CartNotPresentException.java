package dev.pratishtha.project.CartService.exceptions;

public class CartNotPresentException extends RuntimeException {

    public CartNotPresentException(String message) {
        super(message);
    }
}
