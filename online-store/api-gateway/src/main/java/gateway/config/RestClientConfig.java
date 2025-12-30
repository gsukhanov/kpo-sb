package gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ServicesProperties.class)
public class RestClientConfig {

    @Bean
    public RestClient ordersRestClient(ServicesProperties props) {
        return RestClient.builder()
                .baseUrl(props.orders().baseUrl())
                .build();
    }

    @Bean
    public RestClient paymentsRestClient(ServicesProperties props) {
        return RestClient.builder()
                .baseUrl(props.payments().baseUrl())
                .build();
    }
}
