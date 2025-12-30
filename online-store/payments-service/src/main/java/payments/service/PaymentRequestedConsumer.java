package payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import payments.config.KafkaTopicsProperties;
import payments.domains.PaymentInbox;
import payments.kafka.PaymentRequestedEvent;
import payments.kafka.PaymentResultEvent;
import payments.repositories.PaymentInboxRepository;

@Component
@RequiredArgsConstructor
public class PaymentRequestedConsumer {

    private final KafkaTopicsProperties topics;
    private final AccountService accountService;
    private final PaymentInboxRepository inboxRepository;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${payments.kafka.payment-requested-topic:orders.payments.requested}",
            groupId = "${spring.kafka.consumer.group-id:payments-service}"
    )
    @Transactional
    public void handle(String payload, Acknowledgment ack) throws Exception {
        PaymentRequestedEvent event = objectMapper.readValue(payload, PaymentRequestedEvent.class);

        // Pessimistic lock ensures only one consumer instance processes a given orderId at a time.
        PaymentInbox inbox = inboxRepository.findByOrderId(event.orderId())
                .orElseGet(() -> {
                    try {
                        return inboxRepository.save(new PaymentInbox(event.orderId()));
                    } catch (DataIntegrityViolationException e) {
                        // race: someone inserted between our read and insert
                        return inboxRepository.findByOrderId(event.orderId()).orElseThrow();
                    }
                });

        // If already processed, re-enqueue result (best-effort) and ack.
        if (inbox.isProcessed()) {
            boolean success = Boolean.TRUE.equals(inbox.getSuccess());
            PaymentResultEvent res = new PaymentResultEvent(
                    event.orderId(),
                    event.userId(),
                    success,
                    inbox.getReason()
            );

            String outPayload = objectMapper.writeValueAsString(res);
            outboxService.enqueue(topics.paymentResultTopic(), String.valueOf(event.orderId()), outPayload);
            afterCommitAck(ack);
            return;
        }

        WithdrawResult wr = accountService.withdraw(event.userId(), event.amount());
        PaymentResultEvent res = new PaymentResultEvent(event.orderId(), event.userId(), wr.success(), wr.reason());

        inbox.setProcessed(true);
        inbox.setSuccess(wr.success());
        inbox.setReason(wr.reason());
        inbox.setProcessedAt(java.time.OffsetDateTime.now());
        inboxRepository.save(inbox);

        String outPayload = objectMapper.writeValueAsString(res);
        outboxService.enqueue(topics.paymentResultTopic(), String.valueOf(event.orderId()), outPayload);

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
