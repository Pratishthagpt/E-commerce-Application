package dev.pratishtha.project.paymentService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusDto {

    private String paymentId;
    private String paymentStatus;
}
