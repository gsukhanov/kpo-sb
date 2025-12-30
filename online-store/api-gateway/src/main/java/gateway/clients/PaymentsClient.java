package gateway.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class PaymentsClient {
    private final RestClient rc;

    public PaymentsClient(RestClient paymentsRestClient) {
        this.rc = paymentsRestClient;
    }

    public ResponseEntity<String> createAccount(HttpHeaders headers) {
        return doRequest(() -> rc.post()
                .uri("/api/payments/accounts")
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(String.class));
    }

    public ResponseEntity<String> topUp(HttpHeaders headers, Object body) {
        return doRequest(() -> rc.post()
                .uri("/api/payments/accounts/topup")
                .headers(h -> h.addAll(headers))
                .body(body)
                .retrieve()
                .toEntity(String.class));
    }

    public ResponseEntity<String> balance(HttpHeaders headers) {
        return doRequest(() -> rc.get()
                .uri("/api/payments/accounts/balance")
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
