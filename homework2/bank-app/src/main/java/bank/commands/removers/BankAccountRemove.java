package bank.commands.removers;

import bank.commands.interfaces.ICommand;
import bank.commands.interfaces.Invoker;
import bank.facades.BankAccountFacade;
import bank.facades.OperationFacade;
import bank.models.BankAccount;
import bank.models.Operation;

public class BankAccountRemove implements ICommand {
    BankAccount account;

    public BankAccountRemove(BankAccount account) {this.account = account;}

    public Integer execute() {
        for (var pair : OperationFacade.getAll().entrySet()) {
            if (pair.getValue().getBankAccountId().equals(account))
                Invoker.remove(pair.getValue()).execute();
        }
        BankAccountFacade.removeAccount(account.getId());
        return 0;
    }

    public String getName() {
        return "Bank account removal";
    }
}
