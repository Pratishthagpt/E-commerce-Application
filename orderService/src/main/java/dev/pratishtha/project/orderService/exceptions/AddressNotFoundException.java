package dev.pratishtha.project.orderService.exceptions;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String s) {
        super(s);
    }
}
