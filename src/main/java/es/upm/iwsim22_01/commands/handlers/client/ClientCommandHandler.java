package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;

public class ClientCommandHandler implements CommandHandler {
    private static final String
    REMOVE_SUBCOMMAND = "remove",
    ADD_SUBCOMMAND = "add",
    LIST_SUBCOMMAND = "list",
    ERROR_INCORRECT_USE =  "Incorrect use: client add|list|remove";

    private final CommandDispatcher ticketCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE, ERROR_INCORRECT_USE);

    public ClientCommandHandler(ClientService clientService, CashierService cashierService) {
        ticketCommandDispatcher.addCommand(ADD_SUBCOMMAND, new ClientAddCommandHandler(clientService, cashierService));
        ticketCommandDispatcher.addCommand(REMOVE_SUBCOMMAND, new ClientRemoveCommandHandler(clientService));
        ticketCommandDispatcher.addCommand(LIST_SUBCOMMAND, new ClientListCommandHandler(clientService));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        ticketCommandDispatcher.processCommand(tokens);
    }
}
