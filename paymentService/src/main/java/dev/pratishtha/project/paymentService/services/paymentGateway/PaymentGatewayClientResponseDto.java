package dev.pratishtha.project.paymentService.services.paymentGateway;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentGatewayClientResponseDto {

    private String paymentLinkId;
    private String paymentLink;
    private String paymentStatus;
    private int amount;

}
