package bank.commands.tojson;

import bank.commands.interfaces.ICommand;
import org.json.JSONObject;

public abstract class ToJson implements ICommand {
    public abstract JSONObject execute();
}
