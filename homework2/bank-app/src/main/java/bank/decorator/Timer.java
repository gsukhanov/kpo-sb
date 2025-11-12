package bank.decorator;


import bank.commands.interfaces.ICommand;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Timer {
    Duration time = null;
    ICommand command;
    public Timer(ICommand command) {
        this.command = command;
    }
    public Object execute() {
        Instant begin = Instant.now();
        Object obj = command.execute();
        Instant end = Instant.now();
        time = Duration.between(end, begin);
        return obj;
    }
    public Duration getTime() {
        if (time != null) return time;
        throw new RuntimeException("Timer accessed before command executed");
    }
}
