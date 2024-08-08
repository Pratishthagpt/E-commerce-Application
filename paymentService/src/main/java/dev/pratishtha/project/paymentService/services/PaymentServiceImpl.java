package dev.pratishtha.project.paymentService.services;

import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentStatusDto;
import dev.pratishtha.project.paymentService.exceptions.PaymentLinkIdNotFoundException;
import dev.pratishtha.project.paymentService.models.Payment;
import dev.pratishtha.project.paymentService.models.PaymentStatus;
import dev.pratishtha.project.paymentService.repositories.PaymentRepository;
import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGateway;
import dev.pratishtha.project.paymentService.services.paymentGateway.PaymentGatewayClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        PaymentGatewayClientDto clientDto = paymentGateway.createPaymentLink(token, paymentLinkRequestDTO.getOrderId());

        Payment payment = new Payment();
        payment.setPaymentLink(clientDto.getShort_url());
        payment.setAmount(clientDto.getAmount());
        payment.setReferenceId(clientDto.getId());
        payment.setPaymentStatus(PaymentStatus.valueOf(clientDto.getStatus()));

        Payment savedPayment = paymentRepository.save(payment);

        PaymentLinkResponseDTO responseDTO = new PaymentLinkResponseDTO();
        responseDTO.setPaymentLinkId(savedPayment.getReferenceId());
        responseDTO.setPaymentLink(savedPayment.getPaymentLink());
        responseDTO.setPaymentStatus(String.valueOf(savedPayment.getPaymentStatus()));
        responseDTO.setAmount(savedPayment.getAmount());
        responseDTO.setPaymentId(String.valueOf(savedPayment.getUuid()));

        return responseDTO;
    }

    @Override
    public PaymentStatusDto getPaymentStatus(String token, PaymentLinkRequestDTO paymentLinkRequestDTO, String paymentLinkId) {

        PaymentGateway paymentGateway = paymentGatewayChooserStrategy.getBestSuitablePaymentGateway();

        PaymentGatewayClientDto clientDto = paymentGateway.getPaymentStatus(paymentLinkId);

        Optional<Payment> paymentOptional = paymentRepository.findByReferenceId(paymentLinkId);

        if (paymentOptional.isEmpty()) {
            throw new PaymentLinkIdNotFoundException("Unable to fetch the payment details of payment Id - " + "paymentILinkId");
        }

        Payment payment = paymentOptional.get();

        payment.setAmount(clientDto.getAmount());
        payment.setPaymentStatus(PaymentStatus.valueOf(clientDto.getStatus()));

        Payment savedPayment = paymentRepository.save(payment);

        PaymentStatusDto responseDTO = new PaymentStatusDto();
        responseDTO.setPaymentStatus(String.valueOf(savedPayment.getPaymentStatus()));
        responseDTO.setPaymentId(String.valueOf(savedPayment.getUuid()));

        return responseDTO;
    }
}
