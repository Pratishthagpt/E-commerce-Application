package dev.pratishtha.project.emailNotificationService.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class Consumer {

    @KafkaListener(
            topics = "${spring.kafka.topic_1.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeUserNotifications (String userId) {
        System.out.println("Received notification for user - " + userId);

//        send email to user using third party eg. twilio

    }

    @KafkaListener(
            topics = "${spring.kafka.topic_2.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderNotifications (String order) {
        System.out.println("Received notification for user on order - " + order);

//        send email to user for order status using third party eg. twilio

    }

    @KafkaListener(
            topics = "${spring.kafka.topic_3.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeCartNotifications (String cart) {
        System.out.println("Received notification for user on cart - " + cart);

//        send email to user for cart status using third party eg. twilio

    }

    @KafkaListener(
            topics = "${spring.kafka.topic_4.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePaymentNotifications (String paymentLinkDetails) {
        System.out.println("Received notification for user on payment details for processing payment - " + paymentLinkDetails);

//        send email to user for cart status using third party eg. twilio

    }

}
