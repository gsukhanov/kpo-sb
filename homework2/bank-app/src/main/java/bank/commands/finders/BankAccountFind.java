package bank.commands.finders;

import bank.commands.interfaces.ICommand;
import bank.facades.BankAccountFacade;
import bank.models.BankAccount;

public class BankAccountFind implements ICommand {
    String name;

    public BankAccountFind(String name) {this.name = name;}

    @Override
    public BankAccount execute() {
        return BankAccountFacade.findAccount(name);
    }

    @Override
    public String getName() {
        return "Bank account search";
    }
}
