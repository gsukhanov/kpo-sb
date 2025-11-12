package bank.commands.interfaces;

public interface ICommand {
    Object execute();
    String getName();
}
