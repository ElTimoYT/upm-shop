package es.upm.iwsim22_01.commands.handlers;

import es.upm.iwsim22_01.commands.CommandTokens;

public interface CommandHandler {

    void runCommand(CommandTokens tokens);
}
