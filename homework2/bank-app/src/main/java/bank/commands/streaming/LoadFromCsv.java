package bank.commands.streaming;

import bank.commands.interfaces.ICommand;
import bank.streamer.Streamer;

import java.io.IOException;

public class LoadFromCsv implements ICommand {
    Streamer streamer;

    public LoadFromCsv(Streamer streamer) {this.streamer = streamer;}

    public String execute() {
        try {
            streamer.downloadCsv();
            return streamer.getFile(".csv").getName();
        } catch (IOException e) {
            System.out.println("Error on download: " + e);
        }
        return null;
    }

    public String getName() {
        return "Download of database from csv";
    }
}
