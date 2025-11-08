package streaming.uploaders;

import interfaces.IBankAccount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BankAccountUploader {
    File workdir;

    public BankAccountUploader(String path) {
        workdir = new File(path);
        if (!workdir.isDirectory()) {
            workdir = null;
            throw new IllegalArgumentException("No such path: " + path);
        }
    }

    public void upload(ArrayList<IBankAccount> accounts) {
        File csv = new File(workdir, "BankAccounts.csv");
        try {
            if (csv.createNewFile()) {

            }
        } catch (IOException e) {
            System.
            System.exit(-1);
        }
    }

}
