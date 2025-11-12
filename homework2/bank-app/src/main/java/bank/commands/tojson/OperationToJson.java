package bank.commands.tojson;

import bank.models.Operation;
import org.json.JSONObject;

public class OperationToJson extends ToJson {
    Operation operation;

    public OperationToJson(Operation operation) {this.operation = operation;};

    @Override
    public JSONObject execute() {
        JSONObject obj = new JSONObject();
        obj.accumulate("Id", operation.getId());
        obj.accumulate("Type", operation.getType());
        obj.accumulate("Account Id", operation.getBankAccountId().getId());
        obj.accumulate("Amount", operation.getAmount());
        obj.accumulate("Date", operation.getDate());
        obj.accumulate("Category", operation.getCategory());
        obj.accumulate("Description", operation.getDescription());
        return obj;
    }

    public String getName() {
        return "Operation to json conversion";
    }
}
