package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.commands.CommandStatus;

public class EchoCommandHandler implements CommandHandler {

    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: echo \"<message>\"");
        }

        System.out.println("echo \"" + tokens.next() + "\"");
        return new CommandStatus(true);
    }
}
