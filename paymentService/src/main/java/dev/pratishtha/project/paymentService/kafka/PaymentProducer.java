package dev.pratishtha.project.paymentService.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private NewTopic topic;

    @Autowired
    public PaymentProducer(KafkaTemplate<String, Object> kafkaTemplate, NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendNotification (String paymentLinkDetails) {

//        printing out the details of user event
        System.out.println(paymentLinkDetails);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic.name(), paymentLinkDetails);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message = [" + paymentLinkDetails +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            else {
                System.out.println("Unable to send message = [" + paymentLinkDetails +
                        "] due to : " + ex.getMessage());
            }
        });
    }
}
