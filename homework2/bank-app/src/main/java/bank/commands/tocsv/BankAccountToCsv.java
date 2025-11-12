package bank.commands.tocsv;

import bank.models.BankAccount;

public class BankAccountToCsv extends ToCsv {
    BankAccount account;

    public BankAccountToCsv(BankAccount account) {this.account = account;}

    public String execute() {
        return account.getId() + ";" + account.getName() + ";" + account.getBalance() + "\n";
    }

    public String getName() {
        return "Bank account to csv conversion";
    }
}
