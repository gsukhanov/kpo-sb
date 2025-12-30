package payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import payments.domains.PaymentInbox;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface PaymentInboxRepository extends JpaRepository<PaymentInbox, Long> {
    boolean existsByOrderId(Long orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PaymentInbox> findByOrderId(Long orderId);
}
