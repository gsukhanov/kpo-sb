package bank.commands.builders;

import bank.commands.interfaces.ICommand;
import bank.facades.IFacade;
import bank.factories.CategoryFactory;
import bank.factories.OperationFactory;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;
import bank.utils.OperationType;

public class OperationBuild implements ICommand {
    OperationType type;
    BankAccount bankAccountId;
    int amount;
    Date date;
    String description;
    Category category;

    public OperationBuild(OperationType type, String accountName, int amount, Date date, String categoryName) {
        this.type = type;
        this.bankAccountId = (BankAccount) IFacade.find(BankAccount.class, accountName);
        this.amount = amount;
        this.date = date;
        this.description = null;
        this.category = (Category) IFacade.find(Category.class, categoryName);
    }

    public OperationBuild(OperationType type, String accountName, int amount, Date date, String categoryName, String description) {
        this.type = type;
        this.bankAccountId = (BankAccount) IFacade.find(BankAccount.class, accountName);
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = (Category) IFacade.find(Category.class, categoryName);
    }

    @Override
    public Operation execute() {
        Operation operation = OperationFactory.create(type, bankAccountId, amount, date, category);
        if (description != null) operation.setDescription(description);
        return operation;
    }

    public String getName() {
        return "New operation creation";
    }
}
