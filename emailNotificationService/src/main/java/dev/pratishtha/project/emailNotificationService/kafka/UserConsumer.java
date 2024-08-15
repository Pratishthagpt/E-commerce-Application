package dev.pratishtha.project.emailNotificationService.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class UserConsumer {

    @KafkaListener(
            topics = "${spring.kafka.topic_1.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrder (String userId) {
        System.out.println("Received notification for user - " + userId);

//        send email to user using third party eg. twilio

    }

}
