package bank.commands.net;

import bank.commands.interfaces.ICommand;
import bank.facades.IFacade;
import bank.models.BankAccount;
import bank.models.Category;

public class GetNetCategory implements ICommand {
    BankAccount account;
    Category category;

    public GetNetCategory(BankAccount account, Category category) {
        this.account = account;
        this.category = category;
    }

    public Integer execute() {return IFacade.netCategory(account, category);}

    public String getName() {
        return "Category net calculation";
    }
}
