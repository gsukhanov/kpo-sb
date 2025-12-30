package orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import orders.domains.OrderStatus;
import orders.kafka.PaymentResultEvent;
import orders.repositories.OrderRepository;

@Component
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "${orders.kafka.payment-result-topic:payments.orders.result}",
            groupId = "${spring.kafka.consumer.group-id:orders-service}"
    )
    @Transactional
    public void handle(String payload, Acknowledgment ack) throws Exception {
        PaymentResultEvent event = objectMapper.readValue(payload, PaymentResultEvent.class);

        OrderStatus finalStatus = event.success() ? OrderStatus.FINISHED : OrderStatus.CANCELLED;

        // Idempotent: update only if still pending
        orderRepository.markStatusIfCurrent(event.orderId(), OrderStatus.NEW, finalStatus, java.time.OffsetDateTime.now());

        afterCommitAck(ack);
    }

    private void afterCommitAck(Acknowledgment ack) {
        if (ack == null) return;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ack.acknowledge();
            }
        });
    }
}
