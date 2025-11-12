package bank.commands.tojson;

import bank.models.BankAccount;
import org.json.JSONObject;

public class BankAccountToJson extends ToJson {
    BankAccount account;

    public BankAccountToJson(BankAccount account) {this.account = account;}

    @Override
    public JSONObject execute() {
        JSONObject obj = new JSONObject();
        obj.accumulate("Id", account.getId());
        obj.accumulate("Name", account.getName());
        obj.accumulate("Balance", account.getBalance());
        return obj;
    }

    public String getName() {
        return "Bank account to json conversion";
    }
}
