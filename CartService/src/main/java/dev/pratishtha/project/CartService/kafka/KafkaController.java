package dev.pratishtha.project.CartService.kafka;

import dev.pratishtha.project.CartService.dtos.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class KafkaController {

    private CartProducer cartProducer;

    @Autowired
    public KafkaController(CartProducer cartProducer) {
        this.cartProducer = cartProducer;
    }

    @PostMapping("/status/send")
    public ResponseEntity<NotificationDto> sendNotificationForOrderStatus (
            @RequestBody String cart) {

        cartProducer.sendNotification(cart);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("Your cart has been updated - " + cart);
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }
}
