package bank.commands.streaming;

import bank.commands.interfaces.ICommand;
import bank.streamer.Streamer;

import java.io.IOException;

public class LoadFromJson implements ICommand {
    Streamer streamer;

    public LoadFromJson(Streamer streamer) {this.streamer = streamer;}

    public String execute() {
        try {
            streamer.downloadJson();
            return streamer.getFile(".json").getName();
        } catch (IOException e) {
            System.out.println("Error on download: " + e);
        }
        return null;
    }

    public String getName() {
        return "Download of database from json";
    }
}
