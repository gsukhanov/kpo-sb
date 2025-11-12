package bank.models;

import bank.facades.BankAccountFacade;
import bank.factories.BankAccountFactory;

public class BankAccount {
    int id;
    String name;
    int balance;

    public BankAccount(int id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
}
