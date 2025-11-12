package bank.commands.tocsv;

import bank.models.Operation;

public class OperationToCsv extends ToCsv {
    Operation operation;

    public OperationToCsv(Operation operation) {this.operation = operation;}

    public String execute() {
        return operation.getId() + ";"
                + operation.getType() + ";"
                + operation.getBankAccountId().getId() + ";"
                + operation.getAmount() + ";"
                + operation.getDate() + ";"
                + operation.getCategory().getName() + ";"
                + operation.getDescription() + "\n";
    }

    public String getName() {
        return "Operation to csv conversion";
    }
}
