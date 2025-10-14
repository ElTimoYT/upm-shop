package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.CommandStatus;

public class ExitCommandHandler implements CommandHandler {

    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        App.exitMenu();

        return new CommandStatus(true);
    }
}
