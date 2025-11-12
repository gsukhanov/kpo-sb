package bank.commands.fromjson;

import bank.commands.interfaces.ICommand;
import org.json.JSONObject;

public abstract class FromJson implements ICommand {
    JSONObject obj;

    public FromJson(JSONObject obj) {this.obj = obj;}

    public abstract Object execute();
}
