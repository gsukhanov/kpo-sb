package bank.commands.net;

import bank.commands.interfaces.ICommand;
import bank.facades.IFacade;
import bank.models.BankAccount;
import bank.utils.Date;

public class GetNetPeriod implements ICommand {
    Date begin;
    Date end;
    BankAccount account;

    public GetNetPeriod(Date begin, Date end, BankAccount account) {
        this.begin = begin;
        this.end = end;
        this.account = account;
    }

    public Integer execute() {
        return IFacade.net(account, begin, end);
    }

    public String getName() {
        return "Period net calculation";
    }
}
