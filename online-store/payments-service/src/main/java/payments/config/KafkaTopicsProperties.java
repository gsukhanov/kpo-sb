package payments.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payments.kafka")
public record KafkaTopicsProperties(
        String paymentRequestedTopic,
        String paymentResultTopic
) {}
