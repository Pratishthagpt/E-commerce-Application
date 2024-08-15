package dev.pratishtha.project.userService.kafka;

import dev.pratishtha.project.userService.dtos.UserDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class UserProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private NewTopic topic;

    @Autowired
    public UserProducer(KafkaTemplate<String, Object> kafkaTemplate, NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendNotification (String user) {

//        printing out the details of user event
        System.out.println(user);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic.name(), user);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message = [" + user +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            else {
                System.out.println("Unable to send message = [" + user +
                        "] due to : " + ex.getMessage());
            }
        });

    }
}
