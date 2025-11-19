package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.TicketManager;

public class CashierCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER = "Incorrect use: cashier add|remove|list|tickets",

            ADD = "add",
            REMOVE = "remove",
            LIST = "list",
            TICKETS = "tickets";

    private final CommandDispatcher cashierCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE_CASHIER, ERROR_INCORRECT_USE_CASHIER);

    public CashierCommandHandler(CashierManager cashierManager, TicketManager ticketManager) {
        cashierCommandDispatcher.addCommand(ADD, new CashierAddCommandHandler(cashierManager));
        cashierCommandDispatcher.addCommand(REMOVE, new CashierRemoveCommandHandler(cashierManager, ticketManager));
        cashierCommandDispatcher.addCommand(LIST, new CashierListCommandHandler(cashierManager));
        cashierCommandDispatcher.addCommand(TICKETS, new CashierShowTicketCommandHandler(cashierManager));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        cashierCommandDispatcher.processCommand(tokens);
    }

}
