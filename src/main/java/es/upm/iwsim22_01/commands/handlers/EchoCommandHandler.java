package es.upm.iwsim22_01.commands.handlers;

import es.upm.iwsim22_01.commands.CommandTokens;

public class EchoCommandHandler implements CommandHandler {
    private static final String ERROR_INCORRECT_USE = "Incorrect use: echo \"<message>\"",
            ECHO_MESSAGE = "echo \"%s\"";

    @Override
    public void runCommand(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
        }

        System.out.printf(ECHO_MESSAGE, tokens.next());
        System.out.println();
    }
}
