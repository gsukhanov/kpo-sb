package payments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payments.domains.OutboxMessage;
import payments.domains.OutboxStatus;
import payments.repositories.OutboxRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public void enqueue(String topic, String kafkaKey, String payload) {
        outboxRepository.save(new OutboxMessage(topic, kafkaKey, payload));
    }

    @Transactional
    public List<OutboxMessage> acquireBatch(int limit) {
        List<OutboxMessage> rows = outboxRepository.lockNew(limit);
        for (OutboxMessage m : rows) {
            m.setStatus(OutboxStatus.PROCESSING);
            m.setUpdatedAt(OffsetDateTime.now());
        }
        outboxRepository.saveAll(rows);
        return rows;
    }

    @Transactional
    public void markSent(long id) {
        OutboxMessage m = outboxRepository.findById(id).orElse(null);
        if (m == null) return;
        m.setStatus(OutboxStatus.SENT);
        m.setSentAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        outboxRepository.save(m);
    }

    @Transactional
    public void markFailed(long id, String error) {
        OutboxMessage m = outboxRepository.findById(id).orElse(null);
        if (m == null) return;
        m.setStatus(OutboxStatus.NEW); // retry later
        m.setAttempts((m.getAttempts() == null ? 0 : m.getAttempts()) + 1);
        m.setLastError(error);
        m.setUpdatedAt(OffsetDateTime.now());
        outboxRepository.save(m);
    }
}
