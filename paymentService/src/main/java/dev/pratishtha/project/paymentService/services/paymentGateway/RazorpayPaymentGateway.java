package dev.pratishtha.project.paymentService.services.paymentGateway;

import com.razorpay.PaymentLink;
import dev.pratishtha.project.paymentService.exceptions.InvalidUserAuthenticationException;
import dev.pratishtha.project.paymentService.exceptions.PaymentLinkIdNotFoundException;
import dev.pratishtha.project.paymentService.orderServiceClient.OrderDetailsService;
import dev.pratishtha.project.paymentService.orderServiceClient.OrderResponseDto;
import dev.pratishtha.project.paymentService.security.JwtData;
import dev.pratishtha.project.paymentService.security.TokenValidator;
import org.json.JSONObject;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    public PaymentGatewayClientDto createPaymentLink(String token, String orderId) {

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

            PaymentGatewayClientDto responseDto = convertpaymentLinkToPaymentGatewayClientDto(paymentLink);

            return responseDto;
        }
        catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment link.", e);
        }
    }

    public PaymentGatewayClientDto getPaymentStatus (String paymentILinkId) {

        try {
            PaymentLink payment = razorpayClient.paymentLink.fetch(paymentILinkId);

            PaymentGatewayClientDto clientDto = convertpaymentLinkToPaymentGatewayClientDto(payment);

            return clientDto;
        }
        catch (RazorpayException e) {
            throw new PaymentLinkIdNotFoundException("Unable to fetch the payment details of payment Id - " + paymentILinkId);
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

    private PaymentGatewayClientDto convertpaymentLinkToPaymentGatewayClientDto(PaymentLink paymentLink) {

        PaymentGatewayClientDto clientDto = new PaymentGatewayClientDto();

        clientDto.setShort_url(paymentLink.get("short_url"));
        clientDto.setId(paymentLink.get("id"));
        clientDto.setStatus(paymentLink.get("status"));
        clientDto.setAmount_paid(paymentLink.get("amount_paid"));
        clientDto.setPayments(null);
        clientDto.setAmount(paymentLink.get("amount"));
        clientDto.setCancelled_at(paymentLink.get("cancelled_at"));
        clientDto.setCustomer(null);
        clientDto.setDescription(paymentLink.get("description"));
        clientDto.setCurrency(paymentLink.get("currency"));

//        Date expire_by = Date.from(Instant.ofEpochSecond(paymentLink.get("expire_by")));
        clientDto.setExpire_by(null);

//        Date expired_at = Date.from(Instant.ofEpochSecond( paymentLink.get("expired_at") ));
        clientDto.setExpired_at(null);

//        Date updated_at = Date.from(Instant.ofEpochSecond( paymentLink.get("updated_at") ));
        clientDto.setUpdated_at(null);

        clientDto.setNotify(null);

//        Date created_At = Date.from(Instant.ofEpochSecond(paymentLink.get("created_at")));
        clientDto.setCreated_at(null);

        clientDto.setOrder_id(paymentLink.get("order_id"));
        clientDto.setReference_id(paymentLink.get("reference_id"));
        clientDto.setCallbackUrl(paymentLink.get("callback_url"));
        clientDto.setCallbackMethod(paymentLink.get("callback_method"));

        return clientDto;
    }
}
