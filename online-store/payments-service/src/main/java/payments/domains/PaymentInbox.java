package payments.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "payment_inbox",
        uniqueConstraints = @UniqueConstraint(name = "uk_payment_inbox_order_id", columnNames = "order_id")
)
@Getter
@Setter
@NoArgsConstructor
public class PaymentInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "processed", nullable = false)
    private boolean processed = false;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "reason", length = 128)
    private String reason;

    @Column(name = "received_at", nullable = false)
    private OffsetDateTime receivedAt = OffsetDateTime.now();

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    public PaymentInbox(Long orderId) {
        this.orderId = orderId;
        this.receivedAt = OffsetDateTime.now();
    }
}
