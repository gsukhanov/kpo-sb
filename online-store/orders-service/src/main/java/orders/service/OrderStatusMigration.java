package orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Backward-compatible migration for old status names.
 * Needed because earlier versions stored PAYMENT_PENDING/PAID/PAYMENT_FAILED as plain strings.
 */
@Component
@RequiredArgsConstructor
public class OrderStatusMigration {

    private final JdbcTemplate jdbc;

    @EventListener(ApplicationReadyEvent.class)
    public void migrate() {
        // Best-effort: if tables do not exist yet (fresh DB), these updates are harmless.
        try {
            jdbc.update("UPDATE orders SET status='NEW' WHERE status='PAYMENT_PENDING'");
            jdbc.update("UPDATE orders SET status='FINISHED' WHERE status='PAID'");
            jdbc.update("UPDATE orders SET status='CANCELLED' WHERE status='PAYMENT_FAILED'");
        } catch (Exception ignored) {
            // DB may be unavailable during startup or schema not created yet.
        }
    }
}
