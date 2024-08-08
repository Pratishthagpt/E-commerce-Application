package dev.pratishtha.project.paymentService.services;

import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGateway;
import dev.pratishtha.project.paymentService.services.paymentGateway.RazorpayPaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Using strategy pattern to choose the best payment gateway

@Service
public class PaymentGatewayChooserStrategy {

    private RazorpayPaymentGateway razorpayPaymentGateway;

    @Autowired
    public PaymentGatewayChooserStrategy(RazorpayPaymentGateway razorpayPaymentGateway) {
        this.razorpayPaymentGateway = razorpayPaymentGateway;
    }

    public PaymentGateway getBestSuitablePaymentGateway() {
//        some logic to choose the best payment gateway as per business proposal

        return razorpayPaymentGateway;
    }
}
