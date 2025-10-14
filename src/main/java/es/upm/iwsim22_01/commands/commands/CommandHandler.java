package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.commands.CommandStatus;

public interface CommandHandler {

    CommandStatus runCommand(Iterator<String> tokens);
}
