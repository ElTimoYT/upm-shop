package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

public interface CommandHandler {

    void runCommand(Iterator<String> tokens);
}
