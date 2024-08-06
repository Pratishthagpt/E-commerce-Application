package dev.pratishtha.project.paymentService.services;

import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentGatewayChooserStrategy paymentGatewayChooserStrategy;

    @Autowired
    public PaymentServiceImpl(PaymentGatewayChooserStrategy paymentGatewayChooserStrategy) {
        this.paymentGatewayChooserStrategy = paymentGatewayChooserStrategy;
    }

    @Override
    public PaymentLinkResponseDTO generatingPaymentLink(String token, PaymentLinkRequestDTO paymentLinkRequestDTO) {
        PaymentGateway paymentGateway = paymentGatewayChooserStrategy.getBestSuitablePaymentGateway();

        String paymentLink = paymentGateway.createPaymentLink();

        PaymentLinkResponseDTO responseDTO = new PaymentLinkResponseDTO();

        return responseDTO;
    }
}
