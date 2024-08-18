package dev.pratishtha.project.orderService.kafka;

import dev.pratishtha.project.orderService.dtos.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order management for notification API.")
@RestController
@RequestMapping("/api/orders")
public class KafkaController {

    private OrderProducer orderProducer;

    @Autowired
    public KafkaController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @Operation(summary = "API for sending notification for order status to user for particular order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Send notification for order status to user for particular order.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDto.class))
                    })
    })
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
