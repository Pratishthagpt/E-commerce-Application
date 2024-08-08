package dev.pratishtha.project.paymentService.services;

import com.razorpay.RazorpayException;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import dev.pratishtha.project.paymentService.models.Payment;
import dev.pratishtha.project.paymentService.models.PaymentStatus;
import dev.pratishtha.project.paymentService.repositories.PaymentRepository;
import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGateway;
import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGatewayClientResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    private PaymentGatewayChooserStrategy paymentGatewayChooserStrategy;
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentGatewayChooserStrategy paymentGatewayChooserStrategy,
                              PaymentRepository paymentRepository) {
        this.paymentGatewayChooserStrategy = paymentGatewayChooserStrategy;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentLinkResponseDTO generatingPaymentLink(String token, PaymentLinkRequestDTO paymentLinkRequestDTO){
        PaymentGateway paymentGateway = paymentGatewayChooserStrategy.getBestSuitablePaymentGateway();

        PaymentGatewayClientResponseDto clientDto = paymentGateway.createPaymentLink(token, paymentLinkRequestDTO.getOrderId());

        Payment payment = new Payment();
        payment.setPaymentLink(clientDto.getPaymentLink());
        payment.setAmount(clientDto.getAmount());
        payment.setReferenceId(clientDto.getPaymentLinkId());
        payment.setPaymentStatus(PaymentStatus.valueOf(clientDto.getPaymentStatus()));

        Payment savedPayment = paymentRepository.save(payment);

        PaymentLinkResponseDTO responseDTO = new PaymentLinkResponseDTO();
        responseDTO.setPaymentLinkId(savedPayment.getReferenceId());
        responseDTO.setPaymentLink(savedPayment.getPaymentLink());
        responseDTO.setPaymentStatus(String.valueOf(savedPayment.getPaymentStatus()));
        responseDTO.setAmount(savedPayment.getAmount());
        responseDTO.setPaymentId(String.valueOf(savedPayment.getUuid()));

        return responseDTO;
    }
}
