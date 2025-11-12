package bank.runners;


import bank.runners.auxillary.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class ConsoleRunner extends Settings implements CommandLineRunner {

    @Autowired
    public ConsoleRunner(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void run(String... args) {
        System.out.println("[========BANK===APP=======]");
        System.out.println("Welcome to bank app console");
        int input = 5;
        while (input >= 1) {
            System.out.println("Choose a command category:");
            System.out.println("1. Database entry addition/removal.");
            System.out.println("2. Analytics");
            System.out.println("3. Streaming");
            System.out.println("4. Settings");
            System.out.println("0. Exit");
            System.out.println("Enter a number 0-4:");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                input = 5;
            }
            switch (input) {
                case 0: {
                    System.out.println("Goodbye.");
                    return;
                } case 1: {
                    database();
                    break;
                } case 2: {
                    analytics();
                    break;
                } case 3: {
                    streaming();
                    break;
                } case 4: {
                    settings();
                    break;
                } default: {
                    System.out.println("Unknown command. Try again");
                }
            }
        }
    }
}
