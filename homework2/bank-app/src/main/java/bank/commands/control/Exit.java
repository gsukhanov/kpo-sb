package bank.commands.control;

import bank.commands.interfaces.ICommand;

public class Exit implements ICommand {
    public Object execute() {return 0;}

    public String getName() {
        return "Exiting";
    }
}
