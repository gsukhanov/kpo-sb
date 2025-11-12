package bank.commands.fromcsv;

import bank.facades.IFacade;
import bank.factories.OperationFactory;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Date;
import bank.utils.OperationType;

public class OperationFromCsv extends FromCsv {

    public OperationFromCsv(String line) {super(line);}

    public Operation execute() {
        String[] separated = line.split(";");
        int id = Integer.parseInt(separated[0]);
        OperationType type;
        if (separated[1].equals("Gain")) type = OperationType.GAIN;
        else if (separated[1].equals("Loss")) type = OperationType.LOSS;
        else throw new IllegalArgumentException ("Incorrect category type. Allowed types are \"Gain\" and \"Loss\"\n");
        int accountId = Integer.parseInt(separated[2]);
        BankAccount account = (BankAccount) IFacade.find(BankAccount.class, accountId);
        int amount = Integer.parseInt(separated[3]);
        Date date = Date.parse(separated[4]);
        Category category = (Category) IFacade.find(Category.class, separated[5]);
        String description = separated[5];
        Operation operation = OperationFactory.create(id, type, account, amount, date, category);
        operation.setDescription(description);
        return operation;
    }

    public String getName() {
        return "Extraction of operation from csv";
    }
}
