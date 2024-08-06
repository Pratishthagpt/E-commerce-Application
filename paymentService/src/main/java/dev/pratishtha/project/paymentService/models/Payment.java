package dev.pratishtha.project.paymentService.models;


import lombok.Getter;

public class Payment extends BaseModel{

    private String orderId;
    private String referenceId;
    private PaymentStatus paymentStatus;
    private int amount;
}
