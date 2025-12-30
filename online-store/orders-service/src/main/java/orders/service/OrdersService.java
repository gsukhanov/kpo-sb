package orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import orders.config.KafkaTopicsProperties;
import orders.domains.Order;
import orders.domains.OrderIdempotencyKey;
import orders.dto.OrderResponse;
import orders.kafka.PaymentRequestedEvent;
import orders.repositories.OrderIdempotencyRepository;
import orders.repositories.OrderRepository;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrderIdempotencyRepository idempotencyRepository;
    private final OutboxService outboxService;
    private final KafkaTopicsProperties topics;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderResponse createOrder(int userId, String idemKey, long amount) throws Exception {
        var existingKey = idempotencyRepository.findByUserIdAndIdemKey(userId, idemKey);
        if (existingKey.isPresent()) {
            return getOrder(userId, existingKey.get().getOrderId());
        }

        Order order = orderRepository.save(new Order(userId, amount));

                try {
            idempotencyRepository.save(new OrderIdempotencyKey(userId, idemKey, order.getId()));
        } catch (DataIntegrityViolationException e) {
            // concurrent duplicate: do not leave extra order in DB
            orderRepository.deleteById(order.getId());
            var key = idempotencyRepository.findByUserIdAndIdemKey(userId, idemKey)
                    .orElseThrow();
            return getOrder(userId, key.getOrderId());
        }

        PaymentRequestedEvent ev = new PaymentRequestedEvent(order.getId(), userId, amount);
        String payload = objectMapper.writeValueAsString(ev);

        // Transactional Outbox: saved in the same TX as the order
        outboxService.enqueue(topics.paymentRequestedTopic(), String.valueOf(userId), payload);

        return new OrderResponse(order.getId(), order.getUserId(), order.getAmount(), order.getStatus());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(int userId, long orderId) {
        Order o = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found"));
        return new OrderResponse(o.getId(), o.getUserId(), o.getAmount(), o.getStatus());
    }
}
