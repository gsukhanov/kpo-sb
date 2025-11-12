package bank.commands.adders;

import bank.commands.interfaces.ICommand;
import bank.facades.OperationFacade;
import bank.models.Operation;

public class OperationAdd implements ICommand {
    Operation operation;

    public OperationAdd(Operation operation) {this.operation = operation;}

    public Operation execute() {
        OperationFacade.addOperation(operation);
        return operation;
    }

    public String getName() {
        return "Operation database addition";
    }
}
