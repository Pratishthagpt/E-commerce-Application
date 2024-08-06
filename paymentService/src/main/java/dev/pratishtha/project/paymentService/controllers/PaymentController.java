package dev.pratishtha.project.paymentService.controllers;

import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import dev.pratishtha.project.paymentService.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
//    first initiating payment, which will return us the payment link
    public ResponseEntity<PaymentLinkResponseDTO> generatingPaymentLink (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody PaymentLinkRequestDTO paymentLinkRequestDTO
    ) {
        PaymentLinkResponseDTO paymentLinkResponseDTO = paymentService.generatingPaymentLink(token, paymentLinkRequestDTO);

        return new ResponseEntity<>(paymentLinkResponseDTO, HttpStatus.CREATED);
    }
}
