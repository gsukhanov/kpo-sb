package orders.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orders.kafka")
public record KafkaTopicsProperties(
        String paymentRequestedTopic,
        String paymentResultTopic
) {}
