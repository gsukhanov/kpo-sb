package bank.commands.adders;

import bank.commands.interfaces.ICommand;
import bank.facades.BankAccountFacade;
import bank.models.BankAccount;

public class BankAccountAdd implements ICommand {
    BankAccount account;

    public BankAccountAdd(BankAccount account) {this.account = account;}

    public BankAccount execute() {
        BankAccountFacade.addAccount(account);
        return account;
    }

    public String getName() {
        return "Bank account database addition";
    }
}
