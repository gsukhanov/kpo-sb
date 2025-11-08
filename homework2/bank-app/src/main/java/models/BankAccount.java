package models;

import interfaces.IBankAccount;

public class BankAccount implements IBankAccount {
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
