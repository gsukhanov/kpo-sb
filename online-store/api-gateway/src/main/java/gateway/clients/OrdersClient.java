package gateway.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OrdersClient {
    private final RestClient rc;

    public OrdersClient(RestClient ordersRestClient) {
        this.rc = ordersRestClient;
    }

    public ResponseEntity<String> createOrder(HttpHeaders headers, Object body) {
        return doRequest(() -> rc.post()
                .uri("/api/orders")
                .headers(h -> h.addAll(headers))
                .body(body)
                .retrieve()
                .toEntity(String.class));
    }

    public ResponseEntity<String> listOrders(HttpHeaders headers) {
        return doRequest(() -> rc.get()
                .uri("/api/orders")
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(String.class));
    }

    public ResponseEntity<String> getOrder(HttpHeaders headers, String orderId) {
        return doRequest(() -> rc.get()
                .uri("/api/orders/{id}", orderId)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(String.class));
    }

    private ResponseEntity<String> doRequest(RequestCall call) {
        try {
            return call.call();
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body("Upstream request failed: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface RequestCall {
        ResponseEntity<String> call();
    }
}
