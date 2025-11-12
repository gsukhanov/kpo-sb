package bank.commands.removers;

import bank.commands.interfaces.ICommand;
import bank.facades.OperationFacade;
import bank.models.Operation;

public class OperationRemove implements ICommand {
    Operation operation;

    public OperationRemove(Operation operation) {this.operation = operation;}

    public Integer execute() {
        OperationFacade.removeOperation(operation.getId());
        return 0;
    }

    public String getName() {
        return "Operation removal";
    }
}
