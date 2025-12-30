package orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import orders.domains.Order;
import orders.domains.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByIdDesc(Integer userId);

    Optional<Order> findByIdAndUserId(Long id, Integer userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Order o SET o.status = :newStatus, o.updatedAt = :now " +
            "WHERE o.id = :id AND o.status = :expectedStatus")
    int markStatusIfCurrent(
            @Param("id") long id,
            @Param("expectedStatus") OrderStatus expectedStatus,
            @Param("newStatus") OrderStatus newStatus,
            @Param("now") OffsetDateTime now
    );
}
