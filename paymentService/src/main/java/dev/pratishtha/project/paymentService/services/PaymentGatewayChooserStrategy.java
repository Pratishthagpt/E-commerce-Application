package dev.pratishtha.project.paymentService.services;

import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGateway;
import dev.pratishtha.project.paymentService.services.paymentGateway.RazorpayPaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Using strategy pattern to choose the best payment gateway

@Service
public class PaymentGatewayChooserStrategy {

    private PaymentGateway paymentGateway;

    @Autowired
    public PaymentGatewayChooserStrategy(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public PaymentGateway getBestSuitablePaymentGateway() {
//        some logic to choose the best payment gateway as per business proposal

        PaymentGateway bestPaymentGateway = new RazorpayPaymentGateway();
        return bestPaymentGateway;
    }
}
