package dev.pratishtha.project.paymentService.exceptions;

public class PaymentLinkIdNotFoundException extends RuntimeException {
    public PaymentLinkIdNotFoundException(String s) {
        super(s);
    }
}
