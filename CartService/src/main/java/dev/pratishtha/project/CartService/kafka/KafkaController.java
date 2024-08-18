package dev.pratishtha.project.CartService.kafka;

import dev.pratishtha.project.CartService.dtos.ExceptionDTO;
import dev.pratishtha.project.CartService.dtos.GenericCartDTO;
import dev.pratishtha.project.CartService.dtos.NotificationDto;
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

@Tag(name = "Cart management for notification API.")
@RestController
@RequestMapping("/api/carts")
public class KafkaController {

    private CartProducer cartProducer;

    @Autowired
    public KafkaController(CartProducer cartProducer) {
        this.cartProducer = cartProducer;
    }

    @Operation(summary = "API for sending notification for cart status to user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Send notification for cart status to user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDto.class))
                    })
    })
    @PostMapping("/status/send")
    public ResponseEntity<NotificationDto> sendNotificationForCartStatus (
            @RequestBody String cart) {

        cartProducer.sendNotification(cart);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("Your cart has been updated - " + cart);
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }
}
