package bank.runners.auxillary;

import bank.commands.interfaces.Invoker;
import bank.facades.IFacade;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;

import java.util.Scanner;

public class Database extends Executor {
    public Database(Scanner scanner) {
        super(scanner);
    }

    protected void database() {
        int input = 3;
        while (input >= 3) {
            System.out.println("Choose a command:");
            System.out.println("1. Add entry");
            System.out.println("2. Remove entry");
            System.out.println("0. Back");
            System.out.println("Enter a number 0-2: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Unknown command. Try again");
                input = 4;
            }
            switch (input) {
                case 0: break;
                case 1: {
                    add();
                    break;
                } case 2: {
                    remove();
                    break;
                } default: {
                    System.out.println("Unknown command, try again");
                }
            }
        }
    }

    private void add() {
        int input = 4;
        while (input >= 4) {
            System.out.println("Choose a command:");
            System.out.println("1. Add account");
            System.out.println("2. Add category");
            System.out.println("3. Add operation");
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
                    System.out.println("Enter bank account name:");
                    String name = scanner.nextLine().trim();
                    try{
                        BankAccount account = (BankAccount) executeCommand(Invoker.bankAccountBuild(name));
                        executeCommand(Invoker.add(account));
                    } catch (Exception e) {
                        System.out.println("Error on creating a bank account " + e.getMessage());
                    }
                    break;
                } case 2: {
                    System.out.println("Enter category name and type (Gain/Loss),");
                    System.out.println("separated by a comma:");
                    String[] split = scanner.nextLine().split(",* ");
                    try{
                        Category category = (Category) executeCommand(Invoker.categoryBuild(split[0], split[1]));
                        executeCommand(Invoker.add(category));
                    } catch (Exception e) {
                        System.out.println("Error on creating a category: " + e.getMessage());
                    }
                    break;
                } case 3: {
                    System.out.println("Enter type (Gain/Loss), bank account name,");
                    System.out.println("operation amount, date of the operation(day.month.year),");
                    System.out.println("category, and a description(optional)");
                    System.out.println("separated by a comma:");
                    String[] split = scanner.nextLine().split(",* ");
                    Operation operation;
                    try {
                        if (split.length > 5) {

                            operation = (Operation) executeCommand(
                                Invoker.operationBuild(
                                        split[0], split[1], Integer.parseInt(split[2]), Date.parse(split[3]), split[4], split[5]));
                        } else {
                            operation = (Operation) executeCommand(
                                    Invoker.operationBuild(
                                            split[0], split[1], Integer.parseInt(split[2]), Date.parse(split[3]), split[4]));
                            executeCommand(Invoker.add(operation));
                        }
                    } catch (Exception e) {
                        System.out.println("Error on creating an operation: " + e.getMessage());
                    }
                    break;
                } default: {
                    System.out.println("Unknown command, try again");
                }
            }
        }
    }

    private void remove() {
        int input = 4;
        while (input >= 4) {
            System.out.println("Please, choose a command:");
            System.out.println("1. Remove account");
            System.out.println("2. Remove category");
            System.out.println("3. Remove operation");
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
                    System.out.println("Enter account name: ");
                    String name = scanner.nextLine().trim();
                    try {
                        executeCommand(Invoker.remove(IFacade.find(BankAccount.class, name)));
                    } catch (Exception e) {
                        System.out.println("Error removing account: " + e.getMessage());
                    }
                } case 2: {
                    System.out.println("Enter category name: ");
                    String name = scanner.nextLine().trim();
                    try {
                        executeCommand(Invoker.remove(IFacade.find(Category.class, name)));
                    }  catch (Exception e) {
                        System.out.println("Error removing category: " + e.getMessage());
                    }
                } case 3: {
                    System.out.println("Enter operation id: ");
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    try {
                        executeCommand(Invoker.remove(IFacade.find(Operation.class, id)));
                    }  catch (Exception e) {
                        System.out.println("Error removing operation: " + e.getMessage());
                    }
                }
            }
        }
    }
}
