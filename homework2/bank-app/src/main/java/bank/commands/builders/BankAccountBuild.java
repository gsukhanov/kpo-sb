package bank.commands.builders;

import bank.commands.interfaces.ICommand;
import bank.factories.BankAccountFactory;
import bank.models.BankAccount;

public class BankAccountBuild implements ICommand {
    String name;

    public BankAccountBuild(String name) {this.name = name;}

    @Override
    public BankAccount execute() {
        return BankAccountFactory.create(name);
    }

    public String getName() {
        return "New bank account creation";
    }
}
