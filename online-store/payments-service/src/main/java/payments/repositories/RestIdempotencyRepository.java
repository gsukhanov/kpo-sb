package payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import payments.domains.RestIdempotencyKey;

public interface RestIdempotencyRepository extends JpaRepository<RestIdempotencyKey, Long> {
    boolean existsByUserIdAndKey(Integer userId, String key);
}
