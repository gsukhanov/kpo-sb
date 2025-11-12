package bank.models;

import bank.facades.OperationFacade;
import bank.factories.OperationFactory;
import bank.utils.OperationType;
import bank.utils.Date;

public class Operation {
    int id;
    OperationType type;
    BankAccount bankAccountId;
    int amount;
    Date date;
    String description;
    Category category;


    public Operation(int id,
                     OperationType type,
                     BankAccount bankAccountId,
                     int amount,
                     Date date,
                     Category category) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = null;
    }

    public int getId() {
        return id;
    }
    public OperationType getType() {
        return type;
    }
    public BankAccount getBankAccountId() {
        return bankAccountId;
    }
    public int getAmount() {
        return amount;
    }
    public Date getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Category getCategory() {
        return category;
    }
}
