package dev.pratishtha.project.paymentService.services;

import com.razorpay.RazorpayException;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    PaymentLinkResponseDTO generatingPaymentLink(String token, PaymentLinkRequestDTO paymentLinkRequestDTO);
}
