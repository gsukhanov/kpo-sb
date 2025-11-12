package bank.commands.streaming;

import bank.commands.interfaces.ICommand;
import bank.streamer.Streamer;

import java.io.IOException;

public class WriteToJson implements ICommand {
    Streamer streamer;

    public WriteToJson(Streamer streamer) {this.streamer = streamer;}

    public String execute() {
        try {
            streamer.uploadJson();
            return streamer.getFile(".json").getName();
        } catch (IOException e) {
            System.out.println("Error on upload: " + e);
        }
        return null;
    }

    public String getName() {
        return "Upload of database to json";
    }
}
