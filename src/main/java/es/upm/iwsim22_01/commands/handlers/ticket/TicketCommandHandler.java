package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.service.TicketService;

public class TicketCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET = "Incorrect use: ticket new|add|remove|print",

            NEW = "new",
            ADD = "add",
            REMOVE = "remove",
            PRINT = "print",
            LIST = "list";

    private final CommandDispatcher ticketCommandDispatcher = new CommandDispatcher(ERROR_INCORRECT_USE_TICKET, ERROR_INCORRECT_USE_TICKET);

    public TicketCommandHandler(TicketService ticketManager, ProductService productManager, CashierService cashierManager, ClientService clientManager) {
        ticketCommandDispatcher.addCommand(NEW, new TicketNewCommandHandler(ticketManager, cashierManager, clientManager));
        ticketCommandDispatcher.addCommand(ADD, new TicketAddCommandHandler(ticketManager, productManager, cashierManager));
        ticketCommandDispatcher.addCommand(REMOVE, new TicketRemoveCommandHandler(ticketManager, productManager, cashierManager));
        ticketCommandDispatcher.addCommand(PRINT, new TicketPrintCommandHandler(ticketManager, cashierManager));
        ticketCommandDispatcher.addCommand(LIST, new TicketListCommandHandler(ticketManager));
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        ticketCommandDispatcher.processCommand(tokens);
    }
}
