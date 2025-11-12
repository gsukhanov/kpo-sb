package bank.factories;

import bank.models.BankAccount;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

public enum BankAccountFactory {
    INSTANCE();

    int id = 0;

    BankAccountFactory() {}
    public static BankAccount create(String name) {
        INSTANCE.id++;
        return new BankAccount(INSTANCE.id, name);
    }

    public static BankAccount create(int id, @NotNull String name) {
        INSTANCE.id = id;
        return new BankAccount(id, name);
    }
}
