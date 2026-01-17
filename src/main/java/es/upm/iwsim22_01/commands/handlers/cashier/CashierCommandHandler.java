package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.CashierInventory;
import es.upm.iwsim22_01.service.inventory.ClientInventory;
import es.upm.iwsim22_01.service.inventory.TicketInventory;

public class CashierCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER = "Incorrect use: cash add|remove|list|tickets",

            ADD = "add",
            REMOVE = "remove",
            LIST = "list",
            TICKETS = "tickets";

    private final CommandDispatcher cashierCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE_CASHIER, ERROR_INCORRECT_USE_CASHIER);

    public CashierCommandHandler(CashierInventory cashierService, ClientInventory clientService, TicketInventory ticketService) {
        cashierCommandDispatcher.addCommand(ADD, new CashierAddCommandHandler(cashierService));
        cashierCommandDispatcher.addCommand(REMOVE, new CashierRemoveCommandHandler(cashierService, clientService, ticketService));
        cashierCommandDispatcher.addCommand(LIST, new CashierListCommandHandler(cashierService));
        cashierCommandDispatcher.addCommand(TICKETS, new CashierShowTicketCommandHandler(cashierService));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        cashierCommandDispatcher.processCommand(tokens);
    }

}
