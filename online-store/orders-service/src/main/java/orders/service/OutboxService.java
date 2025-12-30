package orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import orders.domains.OutboxMessage;
import orders.domains.OutboxStatus;
import orders.repositories.OutboxRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public void enqueue(String topic, String key, String payload) {
        outboxRepository.save(new OutboxMessage(topic, key, payload));
    }

    @Transactional
    public List<OutboxMessage> acquireBatch(int limit) {
        return outboxRepository.lockNew(limit);
    }

    @Transactional
    public void markSent(long id) {
        OutboxMessage m = outboxRepository.findById(id).orElseThrow();
        m.setStatus(OutboxStatus.SENT);
        m.setSentAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        m.setAttempts(m.getAttempts() + 1);
        m.setLastError(null);
        outboxRepository.save(m);
    }

    @Transactional
    public void markFailed(long id, String error) {
        OutboxMessage m = outboxRepository.findById(id).orElseThrow();
        m.setStatus(OutboxStatus.NEW); // retry later
        m.setUpdatedAt(OffsetDateTime.now());
        m.setAttempts(m.getAttempts() + 1);
        m.setLastError(error);
        outboxRepository.save(m);
    }
}
