package dev.pratishtha.project.paymentService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment extends BaseModel{

    private String orderId;

//    can also be called payment id. This is the unique id that is associated with every payment,
//    that we will get from payment gateway
    private String referenceId;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;
    private String paymentLink;
    private int amount;
}
