package dev.pratishtha.project.CartService.exceptions;


public class CartIdNotFoundException extends RuntimeException{

    public CartIdNotFoundException(String message) {
        super(message);
    }
}
