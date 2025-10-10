package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.commands.CommandStatus;

import java.util.Iterator;

public class EchoCommandHandler implements CommandHandler {
    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: echo \"<message>\"");
        }

        System.out.println(tokens.next());
        return new CommandStatus(true);
    }
}
