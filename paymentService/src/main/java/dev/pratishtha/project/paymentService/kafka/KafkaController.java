package dev.pratishtha.project.paymentService.kafka;

import dev.pratishtha.project.paymentService.dtos.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment notification API.")
@RestController
@RequestMapping("/api/payments")
public class KafkaController {

    private PaymentProducer paymentProducer;

    @Autowired
    public KafkaController(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }


    @Operation(summary = "API for sending notification for payment processing to user and completing payment at given link.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Send notification for payment processing to user and completing payment at given link.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDto.class))
                    })
    })
    @PostMapping("/status/send")
    public ResponseEntity<NotificationDto> sendNotificationForPaymentProcessing (
            @RequestBody String paymentLinkDetails) {

        paymentProducer.sendNotification(paymentLinkDetails);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("Your payment has been processed. Please complete the payment at given link - " + paymentLinkDetails);
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }
}
