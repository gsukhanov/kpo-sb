package orders.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "order_idempotency", uniqueConstraints = {
        @UniqueConstraint(name = "uk_order_idem_user_key", columnNames = {"user_id", "idem_key"})
})
@Getter
@Setter
@NoArgsConstructor
public class OrderIdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "idem_key", nullable = false, length = 128)
    private String idemKey;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public OrderIdempotencyKey(int userId, String idemKey, long orderId) {
        this.userId = userId;
        this.idemKey = idemKey;
        this.orderId = orderId;
        this.createdAt = OffsetDateTime.now();
    }
}
