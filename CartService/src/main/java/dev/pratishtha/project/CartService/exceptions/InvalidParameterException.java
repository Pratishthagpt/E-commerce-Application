package dev.pratishtha.project.CartService.exceptions;


public class InvalidParameterException extends RuntimeException{

    public InvalidParameterException(String message) {
        super(message);
    }
}
