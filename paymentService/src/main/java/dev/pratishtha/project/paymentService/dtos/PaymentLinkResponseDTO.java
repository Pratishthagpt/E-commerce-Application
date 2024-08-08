package dev.pratishtha.project.paymentService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkResponseDTO {

    private String paymentId;
    private String paymentLink;
    private String paymentLinkId;
    private String paymentStatus;
    private int amount;
}
