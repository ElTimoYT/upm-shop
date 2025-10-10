package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.commands.CommandStatus;

import java.util.Iterator;

@FunctionalInterface
public interface Command {
    CommandStatus execute(Iterator<String> tokens);
}
