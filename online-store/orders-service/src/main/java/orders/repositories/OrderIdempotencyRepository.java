package orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import orders.domains.OrderIdempotencyKey;

import java.util.Optional;

public interface OrderIdempotencyRepository extends JpaRepository<OrderIdempotencyKey, Long> {
    Optional<OrderIdempotencyKey> findByUserIdAndIdemKey(Integer userId, String idemKey);
}
