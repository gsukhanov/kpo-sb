package payments.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "rest_idempotency",
        uniqueConstraints = @UniqueConstraint(name = "uk_rest_idem_user_key", columnNames = {"user_id", "idem_key"})
)
@Getter
@Setter
@NoArgsConstructor
public class RestIdempotencyKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "idem_key", nullable = false, length = 128)
    private String key;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public RestIdempotencyKey(Integer userId, String key) {
        this.userId = userId;
        this.key = key;
        this.createdAt = OffsetDateTime.now();
    }
}
