package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.CommandStatus;

import java.util.Iterator;

public class ExitCommand implements Command {
    @Override
    public CommandStatus execute(Iterator<String> tokens) {
        App.exitMenu();

        return new CommandStatus(true);
    }
}
