package orders.domains;

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

    @Column(name = "topic", nullable = false, length = 128)
    private String topic;

    @Column(name = "kafka_key", nullable = false, length = 128)
    private String kafkaKey;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private OutboxStatus status;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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
}
