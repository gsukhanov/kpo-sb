package gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services")
public record ServicesProperties(Service orders, Service payments) {
    public record Service(String baseUrl) {}
}
