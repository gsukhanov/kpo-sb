package orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import orders.domains.OutboxMessage;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {

    @Query(
            value = "SELECT * FROM outbox_messages " +
                    "WHERE status IN ('NEW','FAILED') " +
                    "ORDER BY id " +
                    "LIMIT :limit " +
                    "FOR UPDATE SKIP LOCKED",
            nativeQuery = true
    )
    List<OutboxMessage> lockNew(@Param("limit") int limit);
}
