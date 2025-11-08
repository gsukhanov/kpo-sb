package models;

import interfaces.IOperation;
import interfaces.IReport;

import java.util.ArrayList;

public class Report implements IReport {
    ArrayList<IOperation> operations;

    public Report() {
        operations = new ArrayList<IOperation>();
    }

    public void addOperation(IOperation operation) {
        operations.add(operation);
    }

    public void print() {
        operations.forEach();
    }
}
