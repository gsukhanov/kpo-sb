package orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import orders.domains.OutboxMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "${orders.outbox.publish-interval-ms:500}")
    public void publish() {
        List<OutboxMessage> batch = outboxService.acquireBatch(20);
        if (batch.isEmpty()) return;

        for (OutboxMessage m : batch) {
            try {
                kafkaTemplate.send(m.getTopic(), m.getKafkaKey(), m.getPayload())
                        .get(10, TimeUnit.SECONDS);
                outboxService.markSent(m.getId());
            } catch (Exception e) {
                outboxService.markFailed(m.getId(), e.getMessage());
            }
        }
    }
}
