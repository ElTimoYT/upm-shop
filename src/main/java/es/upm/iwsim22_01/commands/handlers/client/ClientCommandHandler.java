package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.commands.handlers.ticket.*;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Client;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ClientCommandHandler implements CommandHandler {
    private static final String
    REMOVE_SUBCOMMAND = "remove",
    ADD_SUBCOMMAND = "add",
    LIST_SUBCOMMAND = "list",
    ERROR_INCORRECT_USE =  "Incorrect use: client add|list|remove";

    private final CommandDispatcher ticketCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE, ERROR_INCORRECT_USE);

    public ClientCommandHandler(ClientManager clientManager, CashierManager cashierManager) {
        ticketCommandDispatcher.addCommand(ADD_SUBCOMMAND, new ClientAddCommandHandler(clientManager, cashierManager));
        ticketCommandDispatcher.addCommand(REMOVE_SUBCOMMAND, new ClientRemoveCommandHandler(clientManager));
        ticketCommandDispatcher.addCommand(LIST_SUBCOMMAND, new ClientListCommandHandler(clientManager));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        ticketCommandDispatcher.processCommand(tokens);
    }
}
