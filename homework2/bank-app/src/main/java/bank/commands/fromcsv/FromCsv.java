package bank.commands.fromcsv;

import bank.commands.interfaces.ICommand;

public abstract class FromCsv implements ICommand {
    String line;

    public FromCsv(String line) {this.line = line;}

    public abstract Object execute();
}
