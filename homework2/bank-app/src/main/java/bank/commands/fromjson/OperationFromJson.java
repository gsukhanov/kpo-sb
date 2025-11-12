package bank.commands.fromjson;

import bank.facades.IFacade;
import bank.factories.OperationFactory;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;
import bank.utils.OperationType;
import org.json.JSONException;
import org.json.JSONObject;

public class OperationFromJson extends FromJson {

    public OperationFromJson(JSONObject obj) {super(obj);}

    public Operation execute() {
        try {int id = obj.getInt("Id");
            OperationType type = null;
            if (obj.getString("Type").equals("Gain")) type = OperationType.GAIN;
            if (obj.getString("Type").equals("Loss")) type = OperationType.LOSS;
            BankAccount account = (BankAccount) IFacade.find(Operation.class, obj.getInt("Account Id"));
            int amount = obj.getInt("Amount");
            Date date = Date.parse(obj.getString("Date"));
            Category category = (Category) IFacade.find(Category.class, obj.getString("Category"));
            String description;
            try {description = obj.getString("Description");}
            catch (JSONException f) {description = null;}
            Operation operation = OperationFactory.create(id, type, account, amount, date, category);
            if (description != null) operation.setDescription(description);
            return operation;
        } catch (JSONException e) {
            throw new IllegalArgumentException("Bank Account JSON parsing error: field unknown");
        }
    }

    public String getName() {
        return "Extraction of operation from json";
    }
}
