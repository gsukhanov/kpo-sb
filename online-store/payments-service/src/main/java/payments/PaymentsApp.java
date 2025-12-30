package payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableScheduling
@EnableKafka
public class PaymentsApp {
    public static void main(String[] args) {
        SpringApplication.run(PaymentsApp.class, args);
    }
}
