package bank.commands.streaming;

import bank.commands.interfaces.ICommand;
import bank.streamer.Streamer;

import java.io.IOException;

public class WriteToCsv implements ICommand {
    Streamer streamer;

    public WriteToCsv(Streamer streamer) {this.streamer = streamer;}

    public String execute() {
        try {
            streamer.uploadCsv();
            return streamer.getFile(".csv").getName();
        } catch (IOException e) {
            System.out.println("Error on upload: " + e);
        }
        return null;
    }

    public String getName() {
        return "Upload of database to csv";
    }
}
