package models;

import enums.OperationType;
import interfaces.IBankAccount;
import interfaces.ICategory;
import interfaces.IOperation;
import records.Date;

public class Operation implements IOperation {
    int id;
    OperationType type;
    IBankAccount bankAccountId;
    int amount;
    Date date;
    String description;
    ICategory category;

    public Operation(int id,
                     OperationType type,
                     IBankAccount bankAccountId,
                     int amount,
                     Date date,
                     ICategory category) {
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
    public IBankAccount getBankAccountId() {
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
    public ICategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return ;
    }
}
