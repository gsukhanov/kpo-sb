package bank.runners.auxillary;

import bank.commands.interfaces.ICommand;
import bank.decorator.Timer;

import java.util.Scanner;

public class Executor {
    boolean timer = false;
    protected final Scanner scanner;

    public Executor(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void switchTimer() {
        timer = !timer;
    }

    protected Object executeCommand(ICommand command) {
        if (timer) {
            Timer t = new Timer(command);
            Object ret = t.execute();
            try {
                System.out.println(command.getName() + " took " + t.getTime().getSeconds() + " seconds");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
            return ret;
        }
        return command.execute();
    }
}
