package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.commands.CommandStatus;

import java.util.Iterator;

public interface CommandHandler {
    CommandStatus runCommand(Iterator<String> tokens);
}
