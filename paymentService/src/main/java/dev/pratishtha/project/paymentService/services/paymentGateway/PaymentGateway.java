package dev.pratishtha.project.paymentService.services.paymentGateway;

import com.razorpay.RazorpayException;
import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {

    public String createPaymentLink() throws RazorpayException;
}
