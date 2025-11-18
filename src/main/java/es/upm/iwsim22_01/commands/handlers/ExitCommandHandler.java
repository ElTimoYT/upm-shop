package es.upm.iwsim22_01.commands.handlers;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.CommandTokens;

public class ExitCommandHandler implements CommandHandler {

    @Override
    public void runCommand(CommandTokens tokens) {
        App.exitMenu();
    }
}
