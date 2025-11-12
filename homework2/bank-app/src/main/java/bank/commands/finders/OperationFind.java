package bank.commands.finders;

import bank.commands.interfaces.ICommand;
import bank.facades.CategoryFacade;
import bank.facades.OperationFacade;
import bank.models.Category;
import bank.models.Operation;

public class OperationFind implements ICommand {
    int id;

    public OperationFind(int id) {this.id = id;}

    @Override
    public Operation execute() {
        return OperationFacade.findOperation(id);
    }

    @Override
    public String getName() {
        return "Operation search";
    }
}
