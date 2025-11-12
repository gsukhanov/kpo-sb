package bank.runners.auxillary;

import bank.commands.interfaces.Invoker;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;

import java.util.Scanner;

public class Streaming extends Analytics {
    String path = ".";

    public Streaming(Scanner scanner) {
        super(scanner);
    }

    protected void streaming() {
        int input = 4;
        while (input >= 4) {
            System.out.println("Choose a command:");
            System.out.println("1. Change directory (default is current directory)");
            System.out.println("2. Download database");
            System.out.println("3. Upload database");
            System.out.println("0. Back");
            System.out.println("Enter a number 0-2: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                input = 4;
            }
            switch (input) {
                case 0:
                    break;
                case 1: {
                    System.out.println("Enter path to directory: ");
                    path = scanner.nextLine().trim();
                } case 2: {
                    load(false);
                } case 3: {
                    load(true);
                } default: {
                    System.out.println("Unknown command, try again");
                }
            }
        }
    }

    private void load(boolean up) {
        int input = 4;
        Class<?> type = null;
        while (input >= 4) {
            System.out.println("Please, choose a database:");
            System.out.println("1. Bank accounts");
            System.out.println("2. Categories");
            System.out.println("3. Operations");
            System.out.println("0. Back");
            System.out.println("Enter a number 0-3: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                input = 4;
            }
            int input2 = 3;
            String format = "";
            while (input2 >= 3) {
                System.out.println("Please, choose a format:");
                System.out.println("1. .csv");
                System.out.println("2. .json");
                System.out.println("0. Back");
                try {
                    input2 = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Unknown command. Try again");
                    input2 = 3;
                }
                switch (input2) {
                    case 0: {
                        input = 4;
                        break;
                    } case 1: {
                        format = ".csv";
                        break;
                    } case 2: {
                        format = ".json";
                        break;
                    } default: {
                        System.out.println("Unknown command, try again");
                    }
                }
            }
            switch (input) {
                case 0: break;
                case 1: {
                    type = BankAccount.class;
                    break;
                } case 2: {
                    type = Category.class;
                    break;
                } case 3: {
                    type = Operation.class;
                    break;
                } default: {
                    System.out.println("Unknown command, try again");
                }
            }
            try {
                if (up) executeCommand(Invoker.upload(type, path, format));
                else executeCommand(Invoker.download(type, path, format));
            } catch (Exception e) {
                System.out.println("Error on upload: " + e.getMessage());
            }
        }
    }
}