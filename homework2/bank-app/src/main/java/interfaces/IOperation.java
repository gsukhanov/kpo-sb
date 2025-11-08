package interfaces;

import enums.OperationType;
import records.Date;

public interface IOperation {
    int getId();
    OperationType getType();
    IBankAccount getBankAccountId();
    int getAmount();
    Date getDate();
    String getDescription();
    void setDescription(String description);
    ICategory getCategory();
    String toString();
}
