package bank.streamer;

import bank.commands.interfaces.Invoker;
import bank.facades.IFacade;
import bank.models.BankAccount;
import bank.models.Category;
import bank.models.Operation;
import bank.utils.Overwrite;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.TreeMap;

public class Streamer {
    Class<?> type;
    File workdir;

    public Streamer(Class<?> type, @NotNull File workdir) throws IllegalArgumentException {
        this.type = type;
        this.workdir = workdir;
        if (!workdir.isDirectory()) {
            throw new IllegalArgumentException("No such path: " + workdir.getName());
        }
    }

    public void uploadCsv() throws IOException {
        File file = getFile(".csv");
        try (FileWriter writer = new FileWriter(file)) {
            TreeMap<?, ?> objects = IFacade.getAll(type);
            for (var pair : objects.entrySet()) {
                String line = Invoker.toCsv(pair.getValue()).execute();
                writer.write(line);
            }
        }
    }

    public void uploadJson() throws IOException {
        File file = getFile(".json");
        JSONArray entries = new JSONArray();
        TreeMap<?, ?> objects = IFacade.getAll(type);
        for (var pair : objects.entrySet()) {
            JSONObject obj = Invoker.toJson(pair.getValue()).execute();
            entries.put(obj);
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(entries.toString());
        }
    }

    public void downloadCsv() throws IOException {
        File file = getFile(".csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while((line = reader.readLine()) != null) {
                Object obj = Invoker.fromCsv(type, line);
                IFacade.add(obj);
            }
        }
    }

    public void downloadJson() throws IOException {
        File file = getFile(".json");
        JSONArray entities = new JSONArray(file);
        for (int i = 0; i < entities.length(); i++) {
            Object obj = Invoker.fromJson(type, entities.getJSONObject(i));
            IFacade.add(obj);
        }
    }

    public @NotNull File getFile(String format) throws IOException {
        String filename;
        if (type == BankAccount.class)
            filename = "BankAccounts";
        else if (type == Category.class)
            filename = "Categories";
        else if (type == Operation.class)
            filename = "Operations";
        else throw new IllegalArgumentException("Unknown type");
        filename += format;
        File file = new File(workdir, filename);
        if (!(file.createNewFile() || Overwrite.INSTANCE.yes()))
            throw new FileAlreadyExistsException(filename + " already exists!");
        return file;
    }
}
