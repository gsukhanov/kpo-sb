package bank.facades;

import bank.models.*;
import bank.utils.Date;
import bank.utils.OperationType;

import java.util.TreeMap;

public interface IFacade {
    // Model facades

    static TreeMap<?, ?> getAll(Class<?> type) {
        if (type == BankAccount.class) return BankAccountFacade.getAll();
        if (type == Category.class) return CategoryFacade.getAll();
        if (type == Operation.class) return OperationFacade.getAll();
        throw new IllegalArgumentException("Unknown class");
    }
    static Object find(Class<?> type, Object key) {
        if (type == BankAccount.class) {
            if (key instanceof Integer) return BankAccountFacade.findAccount((Integer) key);
            if (key instanceof String) return BankAccountFacade.findAccount((String) key);
            throw new IllegalArgumentException("Incorrect key type");
        }
        if (type == Category.class) return CategoryFacade.findCategory((String) key);
        if (type == Operation.class) return OperationFacade.findOperation((Integer) key);
        throw new IllegalArgumentException("Unknown class");
    }
    static void add(Object obj) {
        if (obj instanceof BankAccount) BankAccountFacade.addAccount((BankAccount) obj);
        if (obj instanceof Category)  CategoryFacade.addCategory((Category) obj);
        if (obj instanceof Operation) OperationFacade.addOperation((Operation) obj);
        throw new IllegalArgumentException("Unknown class");
    }

    // Analytics

    static int net(BankAccount account, Date begin, Date end) {
        int ret = 0;
        TreeMap<Integer, Operation> operations = OperationFacade.getAll();
        for (var pair : operations.entrySet()) {
            if (account.equals(pair.getValue().getBankAccountId())) {
                if (pair.getValue().getType() == OperationType.GAIN) ret += pair.getValue().getAmount();
                else ret -= pair.getValue().getAmount();
            }
        }
        return ret;
    }

    static int netCategory(BankAccount account, Category category) {
        int ret = 0;
        TreeMap<Integer, Operation> operations = OperationFacade.getAll();
        for (var pair : operations.entrySet()) {
            if (account.equals(pair.getValue().getBankAccountId())
                    && category.equals(pair.getValue().getCategory())) {
                if (pair.getValue().getType() == OperationType.GAIN)
                    ret += pair.getValue().getAmount();
                else
                    ret -= pair.getValue().getAmount();
            }
        }
        return ret;
    }
}
