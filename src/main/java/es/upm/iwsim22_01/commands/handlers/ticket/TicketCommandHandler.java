package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.CashierInventory;
import es.upm.iwsim22_01.service.inventory.ClientInventory;
import es.upm.iwsim22_01.service.inventory.ProductInventory;
import es.upm.iwsim22_01.service.inventory.TicketInventory;

public class TicketCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET = "Incorrect use: ticket new|add|remove|print",

            NEW = "new",
            ADD = "add",
            REMOVE = "remove",
            PRINT = "print",
            LIST = "list";

    private final CommandDispatcher ticketCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE_TICKET, ERROR_INCORRECT_USE_TICKET);

    public TicketCommandHandler(TicketInventory ticketService, ProductInventory productService, CashierInventory cashierService, ClientInventory clientService) {
        ticketCommandDispatcher.addCommand(NEW, new TicketNewCommandHandler(ticketService, cashierService, clientService));
        ticketCommandDispatcher.addCommand(ADD, new TicketAddCommandHandler(ticketService, productService, cashierService));
        ticketCommandDispatcher.addCommand(REMOVE, new TicketRemoveCommandHandler(ticketService, productService, cashierService));
        ticketCommandDispatcher.addCommand(PRINT, new TicketPrintCommandHandler(ticketService, cashierService));
        ticketCommandDispatcher.addCommand(LIST, new TicketListCommandHandler(ticketService));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        ticketCommandDispatcher.processCommand(tokens);
    }
}
