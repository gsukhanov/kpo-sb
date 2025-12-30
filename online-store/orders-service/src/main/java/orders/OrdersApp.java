package orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableScheduling
@EnableKafka
public class OrdersApp {
    public static void main(String[] args) {
        SpringApplication.run(OrdersApp.class, args);
    }
}
