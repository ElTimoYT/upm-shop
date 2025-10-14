package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.App;

public class ExitCommandHandler implements CommandHandler {

    @Override
    public void runCommand(Iterator<String> tokens) {
        App.exitMenu();
    }
}
