package dev.pratishtha.project.paymentService.services.paymentGateway;

import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {

    public PaymentGatewayClientDto createPaymentLink(String token, String orderId);
    public PaymentGatewayClientDto getPaymentStatus (String paymentILinkId);

}
