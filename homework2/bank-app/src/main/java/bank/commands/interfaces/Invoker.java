package bank.commands.interfaces;

import bank.commands.adders.*;
import bank.commands.builders.*;
import bank.commands.finders.*;
import bank.commands.net.*;
import bank.commands.removers.*;
import bank.commands.streaming.*;
import bank.commands.tocsv.*;
import bank.commands.tojson.*;
import bank.commands.fromcsv.*;
import bank.commands.fromjson.*;
import bank.models.*;
import bank.streamer.Streamer;
import bank.utils.CategoryType;
import bank.utils.Date;
import bank.utils.OperationType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public interface Invoker {
    static ToCsv toCsv(Object obj) {
        if (obj instanceof BankAccount) return new BankAccountToCsv((BankAccount) obj);
        if (obj instanceof Category) return new CategoryToCsv((Category) obj);
        if (obj instanceof Operation) return new OperationToCsv((Operation) obj);
        throw new IllegalArgumentException("ToCsv invocation failure: unknown type");
    }
    static ToJson toJson(Object obj) throws JSONException {
        if (obj instanceof BankAccount) return new BankAccountToJson((BankAccount) obj);
        if (obj instanceof Category) return new CategoryToJson((Category) obj);
        if (obj instanceof Operation) return new OperationToJson((Operation) obj);
        throw new IllegalArgumentException("ToJson invocation failure: unknown type");
    }
    static FromCsv fromCsv(Class<?> type, String string) {
        if (type == BankAccount.class) return new BankAccountFromCsv(string);
        if (type == Category.class) return new CategoryFromCsv(string);
        if (type == Operation.class) return new OperationFromCsv(string);
        throw new IllegalArgumentException("FromCsv invocation failure: unknown type");
    }
    static FromJson fromJson(Class<?> type, JSONObject object) throws JSONException {
        if (type == BankAccount.class) return new BankAccountFromJson(object);
        if (type == Category.class) return new CategoryFromJson(object);
        if (type == Operation.class) return new OperationFromJson(object);
        throw new IllegalArgumentException("FromJson invocation failure: unknown type");
    }

    static ICommand remove(Object obj) {
        if (obj instanceof BankAccount) return new BankAccountRemove((BankAccount) obj);
        if (obj instanceof Category) return new CategoryRemove((Category) obj);
        if (obj instanceof Operation) return new OperationRemove((Operation) obj);
        throw new IllegalArgumentException("remove invocation failure: unknown type");
    }

    static ICommand add(Object obj) {
        if (obj instanceof BankAccount) return new BankAccountAdd((BankAccount) obj);
        if (obj instanceof Category) return new CategoryAdd((Category) obj);
        if (obj instanceof Operation) return new OperationAdd((Operation) obj);
        throw new IllegalArgumentException("Add invocation failure: unknown type");
    }

    static ICommand find(Class<?> type, Object obj) {
        if (type == BankAccount.class) return new BankAccountFind((String) obj);
        if (type == Category.class) return new CategoryFind((String) obj);
        if (type == Operation.class) return new OperationFind((Integer) obj);
        throw new IllegalArgumentException("Find invocation failure: unknown type");
    }

    static ICommand upload(Class<?> type, String workdir, String format) {
        if (format.equals(".csv")) return new WriteToCsv(new Streamer(type, new File(workdir)));
        if (format.equals(".json")) return new WriteToJson(new Streamer(type, new File(workdir)));
        throw new IllegalArgumentException("Unsupported file type");
    }

    static ICommand download(Class<?> type, String workdir, String format) {
        if (format.equals(".csv")) return new LoadFromCsv(new Streamer(type, new File(workdir)));
        if (format.equals(".json")) return new LoadFromJson(new Streamer(type, new File(workdir)));
        throw new IllegalArgumentException("Unsupported file type");
    }

    static BankAccountBuild bankAccountBuild(String name) {
        return new BankAccountBuild(name);
    }
    static CategoryBuild categoryBuild(String type, String name) {
        if (type.equals("Gain")) return new CategoryBuild(CategoryType.GAIN, name);
        if (type.equals("Loss")) return new CategoryBuild(CategoryType.LOSS, name);
        throw new IllegalArgumentException("Category build error: unknown category type");
    }
    static OperationBuild operationBuild(String type, String accountName, int amount, Date date, String categoryName) {
        if (type.equals("Gain")) return new OperationBuild(OperationType.GAIN, accountName, amount, date, categoryName);
        if (type.equals("Loss")) return new OperationBuild(OperationType.LOSS, accountName, amount, date, categoryName);
        throw new IllegalArgumentException("Category build error: unknown category type");
    }
    static OperationBuild operationBuild(String type, String accountName, int amount, Date date, String categoryName, String description) {
        if (type.equals("Gain")) return new OperationBuild(OperationType.GAIN, accountName, amount, date, categoryName, description);
        if (type.equals("Loss")) return new OperationBuild(OperationType.LOSS, accountName, amount, date, categoryName, description);
        throw new IllegalArgumentException("Category build error: unknown category type");
    }

    static GetNetPeriod getNetPeriod(Date begin, Date end, BankAccount account) {
        return new GetNetPeriod(begin, end, account);
    }

    static GetNetCategory getNetCategory(BankAccount account, Category category) {
        return new GetNetCategory(account, category);
    }
}
