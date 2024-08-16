package dev.pratishtha.project.orderService.kafka;

import dev.pratishtha.project.orderService.dtos.OrderStatusDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private NewTopic topic;

    @Autowired
    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate, NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendNotification (String order) {

//        printing out the details of user event
        System.out.println(order);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic.name(), order);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message = [" + order +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            else {
                System.out.println("Unable to send message = [" + order +
                        "] due to : " + ex.getMessage());
            }
        });
    }
}
