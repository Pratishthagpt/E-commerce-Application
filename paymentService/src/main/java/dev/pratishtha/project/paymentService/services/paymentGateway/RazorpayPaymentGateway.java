package dev.pratishtha.project.paymentService.services.paymentGateway;

import com.razorpay.PaymentLink;
import dev.pratishtha.project.paymentService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.paymentService.models.Payment;
import dev.pratishtha.project.paymentService.models.PaymentStatus;
import dev.pratishtha.project.paymentService.orderServiceClient.OrderDetailsService;
import dev.pratishtha.project.paymentService.orderServiceClient.OrderResponseDto;
import dev.pratishtha.project.paymentService.security.JwtData;
import dev.pratishtha.project.paymentService.security.TokenValidator;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class RazorpayPaymentGateway implements PaymentGateway{

    private OrderDetailsService orderDetailsService;
    private TokenValidator tokenValidator;
    private RazorpayClient razorpayClient;

    @Autowired
    public RazorpayPaymentGateway(OrderDetailsService orderDetailsService,
                                  TokenValidator tokenValidator,
                                  RazorpayClient razorpayClient) {
        this.orderDetailsService = orderDetailsService;
        this.tokenValidator = tokenValidator;
        this.razorpayClient = razorpayClient;
    }

    @Override
    public PaymentGatewayClientResponseDto createPaymentLink(String token, String orderId) {

        JwtData userJwtData = validateUserByToken(token);
        int amount = getPriceFromOrder(token, orderId);
        int amountInPaise = amount * 100;

        try {
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amountInPaise);
            paymentLinkRequest.put("currency","INR");

//        we are assuming that we are not taking partial payments
            paymentLinkRequest.put("accept_partial",false);

//        paymentLinkRequest.put("first_min_partial_amount",100);

//        setting up expiry date from 2 days after
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, 2);

            Date expiryDate = calendar.getTime();
            long expiryDateTimestamp = expiryDate.getTime() / 1000;

            paymentLinkRequest.put("expire_by",expiryDateTimestamp);

            paymentLinkRequest.put("reference_id","TS1989");
            paymentLinkRequest.put("description","Payment for order id " + orderId);

            JSONObject customer = new JSONObject();
            customer.put("name", userJwtData.getUsername());
            customer.put("contact", userJwtData.getPhoneNo());
            customer.put("email",userJwtData.getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("sms",true);
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("reminder_enable",false);

//            creating the local tunnel public url
            String callbackUrl = "https://upset-planes-sit.loca.lt/db/products";

            paymentLinkRequest.put("callback_url", callbackUrl);
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            PaymentGatewayClientResponseDto responseDto = new PaymentGatewayClientResponseDto();
            responseDto.setPaymentLink(paymentLink.get("short_url"));
            responseDto.setPaymentLinkId(paymentLink.get("id"));
            responseDto.setPaymentStatus(paymentLink.get("status"));
            responseDto.setAmount(paymentLink.get("amount_paid"));

            return responseDto;
        }
        catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment link.", e);
        }
    }

    public JwtData validateUserByToken (String token) {
        Optional<JwtData> userData = tokenValidator.validateToken(token);
        if (userData.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        JwtData userJwtData = userData.get();

        return userJwtData;
    }

    public int getPriceFromOrder (String token, String orderId) {
        Optional<OrderResponseDto> orderDetailsOptional = orderDetailsService.getOrderDetails(token, orderId);
        if (orderDetailsOptional.isEmpty()) {
            throw new InvalidUserAuthenticationException("User token is not Authenticated. Please enter the valid authentication token.");
        }

        OrderResponseDto order = orderDetailsOptional.get();

        return order.getTotalPrice();
    }
}
