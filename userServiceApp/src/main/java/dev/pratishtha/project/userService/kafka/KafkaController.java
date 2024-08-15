package dev.pratishtha.project.userService.kafka;

import dev.pratishtha.project.userService.dtos.NotificationDto;
import dev.pratishtha.project.userService.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class KafkaController {

    private UserProducer userProducer;

    @Autowired
    public KafkaController(UserProducer userProducer) {
        this.userProducer = userProducer;
    }

    @PostMapping("/signup/send")
    public ResponseEntity<NotificationDto> sendNotificationForSignUp (@RequestBody String user) {
        userProducer.sendNotification(user);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("User - " + user + " has successfully signed up.");
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }

    @PostMapping("/login/send")
    public ResponseEntity<NotificationDto> sendNotificationForLogin (@RequestBody String user) {
        userProducer.sendNotification(user);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("User - " + user + " has successfully logged in.");
        notificationDto.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(notificationDto, HttpStatus.OK);
    }
}
