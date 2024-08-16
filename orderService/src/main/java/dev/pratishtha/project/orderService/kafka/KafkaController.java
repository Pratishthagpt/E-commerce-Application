package dev.pratishtha.project.orderService.kafka;

import dev.pratishtha.project.orderService.dtos.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class KafkaController {

    private OrderProducer orderProducer;

    @Autowired
    public KafkaController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/status/send")
    public ResponseEntity<NotificationDto> sendNotificationForOrderStatus (
            @RequestBody String order) {

        orderProducer.sendNotification(order);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("Your Order status has been changed - " + order);
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }
}
