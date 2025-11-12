package bank.runners.auxillary;

import bank.commands.interfaces.Invoker;
import bank.facades.BankAccountFacade;
import bank.models.BankAccount;
import bank.models.Category;
import bank.utils.Date;

import java.util.Scanner;

public class Analytics extends Database {
    public Analytics(Scanner scanner) {
        super(scanner);
    }

    protected void analytics() {
        int input = 4;
        while (input >= 4) {
            System.out.println("Choose a command:");
            System.out.println("1. Get balance");
            System.out.println("2. Get net of a period");
            System.out.println("3. Get net of a category");
            System.out.println("0. Back");
            System.out.println("Enter a number 0-3: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                input = 4;
            }
            switch (input) {
                case 0: break;
                case 1: {
                    System.out.println("Enter bank account name: ");
                    String name = scanner.nextLine().trim();
                    try {
                        System.out.println("Balance is $" + ((BankAccount) executeCommand(Invoker.find(BankAccount.class, name))).getBalance());
                    } catch (Exception e) {
                        System.out.println("Error getting balance: " + e.getMessage());
                    }
                    break;
                } case 2: {
                    System.out.println("Enter bank account name: ");
                    String name = scanner.nextLine().trim();
                    try {
                        BankAccount account = (BankAccount) executeCommand(Invoker.find(BankAccount.class, name));
                        System.out.println("Enter first date (DD.MM.YYYY): ");
                        String begin = scanner.nextLine().trim();
                        Date start = Date.parse(begin);
                        System.out.println("Enter second date (DD.MM.YYYY): ");
                        String finish = scanner.nextLine().trim();
                        Date end = Date.parse(finish);
                        System.out.println("Net during said period is $" + executeCommand(Invoker.getNetPeriod(start, end, account)));
                    } catch (Exception e) {
                        System.out.println("Error counting net during period: " + e.getMessage());
                    }
                    break;
                } case 3: {
                    System.out.println("Enter bank account name: ");
                    String name = scanner.nextLine().trim();
                    try {
                        BankAccount account = (BankAccount) executeCommand(Invoker.find(BankAccount.class, name));
                        System.out.println("Enter category name: ");
                        name = scanner.nextLine().trim();
                        Category category = (Category) executeCommand(Invoker.find(Category.class, name));
                        System.out.println("Net of said category is $" + executeCommand(Invoker.getNetCategory(account, category)));
                    } catch (Exception e) {
                        System.out.println("Error finding account: " + e.getMessage());
                    }
                    break;
                } default: {
                    System.out.println("Unknown command, try again");
                }
            }
        }
    }
}
