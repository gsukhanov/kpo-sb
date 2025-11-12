package bank.factories;

import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;
import bank.utils.OperationType;
import org.springframework.stereotype.Component;

public enum OperationFactory {
    INSTANCE;

    int id = 0;

    OperationFactory() {}
    public static Operation create(OperationType type, BankAccount bankAccountId, int amount, Date date, Category category) {
        INSTANCE.id++;
        return new Operation(INSTANCE.id, type, bankAccountId, amount, date, category);
    }

    public static Operation create(int id, OperationType type, BankAccount bankAccountId, int amount, Date date, Category category) {
        INSTANCE.id = id;
        return new Operation(id, type, bankAccountId, amount, date, category);
    }
}
