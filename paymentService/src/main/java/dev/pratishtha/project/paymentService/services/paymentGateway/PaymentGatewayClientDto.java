package dev.pratishtha.project.paymentService.services.paymentGateway;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentGatewayClientDto {

    private int amount;
    private int amount_paid;
    private int cancelled_at;
    private Date created_at;
    private String currency;
    private String customer;
    private String description;
    private Date expire_by;
    private Date expired_at;
    private String id;
    private Object [] notify;
    private String order_id;
    private Object [] payments;
    private String reference_id;
    private String short_url;
    private String status;
    private Date updated_at;
    private String user_id;
    private String callbackMethod;
    private String callbackUrl;

}
