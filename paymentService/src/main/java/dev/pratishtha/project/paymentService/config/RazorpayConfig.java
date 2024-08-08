package dev.pratishtha.project.paymentService.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Value("${razorpay_key_id}")
    private String key;

    @Value("${razorpay_key_secret}")
    private String secret;

    @Bean
    public RazorpayClient getRazorpayClient() {
        try {
            return new RazorpayClient(key, secret);
        }
        catch (RazorpayException e) {
            System.out.println("Unable to create Razorpay client.");
            throw new RuntimeException("Failed to instantiate razorpay client.");
        }
    }
}
