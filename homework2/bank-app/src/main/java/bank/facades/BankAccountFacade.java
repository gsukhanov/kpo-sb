package bank.facades;

import bank.models.BankAccount;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

public enum BankAccountFacade {
    INSTANCE();

    final TreeMap<Integer, BankAccount> accounts;

    BankAccountFacade() {this.accounts = new TreeMap<>();}
    public static BankAccount findAccount(int id) {
        if (INSTANCE.accounts.containsKey(id)) return INSTANCE.accounts.get(id);
        else throw new IndexOutOfBoundsException("No account with such id");
    }
    public static BankAccount findAccount(String name) {
        Integer id = null;
        for (var pair : INSTANCE.accounts.entrySet())
            if (pair.getValue().getName().equals(name)) id = pair.getValue().getId();
        if (id != null) return INSTANCE.accounts.get(id);
        else throw new IndexOutOfBoundsException("No account with such name");
    }
    public static void addAccount(BankAccount account) {
        INSTANCE.accounts.put(account.getId(), account);
    }
    public static void removeAccount(int id) {
        INSTANCE.accounts.remove(id);
    }
    public static TreeMap<Integer, BankAccount> getAll() {
        return INSTANCE.accounts;
    }
}
