package bank.commands.fromcsv;

import bank.factories.BankAccountFactory;
import bank.models.BankAccount;

public class BankAccountFromCsv extends FromCsv {

    public BankAccountFromCsv(String line) {super(line);}

    public BankAccount execute() {
        String[] separated = line.split(";");
        int id = Integer.parseInt(separated[0]);
        String name = separated[1];
        int balance = Integer.parseInt(separated[2]);
        BankAccount account = BankAccountFactory.create(id, name);
        account.setBalance(balance);
        return account;
    }

    public String getName() {
        return "Extraction of bank account from csv";
    }
}
