package bank.commands.fromjson;

import bank.factories.BankAccountFactory;
import org.json.*;
import bank.models.BankAccount;

public class BankAccountFromJson extends FromJson {

    public BankAccountFromJson(JSONObject obj) {super(obj);}

    public BankAccount execute() {
        try {int id = obj.getInt("Id");
            String name = obj.getString("Name");
            int balance = obj.getInt("Balance");
            BankAccount account = BankAccountFactory.create(id, name);
            account.setBalance(balance);
            return account;
        } catch (JSONException e) {
            throw new IllegalArgumentException("Bank Account JSON parsing error: field unknown");
        }
    }

    public String getName() {
        return "Extraction of bank account from json";
    }
}
