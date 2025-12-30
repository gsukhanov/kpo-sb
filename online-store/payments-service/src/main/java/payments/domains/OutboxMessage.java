package payments.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "outbox_messages", indexes = {
        @Index(name = "ix_outbox_status_id", columnList = "status,id")
})
@Getter
@Setter
@NoArgsConstructor
public class OutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "kafka_key", nullable = false)
    private String kafkaKey;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxStatus status = OutboxStatus.NEW;

    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    public OutboxMessage(String topic, String kafkaKey, String payload) {
        this.topic = topic;
        this.kafkaKey = kafkaKey;
        this.payload = payload;
        this.status = OutboxStatus.NEW;
        this.attempts = 0;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getKafkaKey() {
        return kafkaKey;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setKafkaKey(String kafkaKey) {
        this.kafkaKey = kafkaKey;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }

}
