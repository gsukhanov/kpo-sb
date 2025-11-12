package bank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Scanner;

@Configuration
public class AppConfig {
    @Bean
    @Primary
    public Scanner scanner() {return new Scanner(System.in);}
}
