package dev.pratishtha.project.paymentService.kafka;

import dev.pratishtha.project.paymentService.dtos.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class KafkaController {

    private PaymentProducer paymentProducer;

    @Autowired
    public KafkaController(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

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
