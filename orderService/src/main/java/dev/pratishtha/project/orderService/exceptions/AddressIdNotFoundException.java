package dev.pratishtha.project.orderService.exceptions;

public class AddressIdNotFoundException extends RuntimeException {
    public AddressIdNotFoundException(String s) {
        super(s);
    }
}
