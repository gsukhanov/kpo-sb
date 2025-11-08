package facades;

import interfaces.IBankAccount;
import interfaces.IOperation;

public class BankAccountFacade {
    IBankAccount account;


    public BankAccountFacade(IBankAccount account) {
        this.account = account;
    }


}
