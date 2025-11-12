package bank.runners.auxillary;

import bank.utils.Overwrite;

import java.util.Scanner;

public class Settings extends Streaming {
    public Settings(Scanner scanner) {
        super(scanner);
    }

    protected void settings() {
        int input = 3;
        while (input >= 3) {
            System.out.println("Choose a command:");
            System.out.println("1. Switch overwriting databases (default is off)");
            System.out.println("2. Switch time calculation (default is off)");
            System.out.println("0. Back");
            System.out.println("Enter a number 0-2: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                input = 3;
            }
            switch (input) {
                case 0: {
                    break;
                } case 1: {
                    Overwrite.INSTANCE.set(!Overwrite.INSTANCE.yes());
                    break;
                } case 2: {
                    switchTimer();
                    break;
                } default: {
                    System.out.println("Unknown command. Try again");
                }
            }
        }
    }
}
