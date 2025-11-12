package bank.facades;

import bank.models.BankAccount;
import bank.models.Operation;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

public enum OperationFacade {
    INSTANCE();

    final TreeMap<Integer, Operation> operations;

    OperationFacade() {this.operations = new TreeMap<>();}
    public static TreeMap<Integer, Operation> getAll() {
        return INSTANCE.operations;
    }

    public static void addOperation(Operation operation) {
        INSTANCE.operations.put(operation.getId(), operation);
    }
    public static void removeOperation(int id) {
        INSTANCE.operations.remove(id);
    }
    public static Operation findOperation(int id) {
        return INSTANCE.operations.get(id);
    }
}
