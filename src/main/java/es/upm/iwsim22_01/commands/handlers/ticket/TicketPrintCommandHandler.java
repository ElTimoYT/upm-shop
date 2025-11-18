package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

public class TicketPrintCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_PRINT = "Incorrect use: ticket print <ticketId> <cashId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",

            TICKET_PRINT_OK = "ticket print: ok";

    private final TicketManager ticketManager;
    private final CashierManager cashierManager;

    public TicketPrintCommandHandler(TicketManager ticketManager, CashierManager cashierManager) {
        this.ticketManager = ticketManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        //ticketId
        Integer ticketId = getTicketId(tokens);
        if (ticketId == null) return;

        //cashierId
        String cashierId = getCashierId(tokens);
        if (cashierId == null) return;

        Ticket ticket = ticketManager.get(ticketId);
        ticket.closeTicket();

        System.out.println(TICKET_PRINT_OK);
    }

    private String getCashierId(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
            return null;
        }

        String cashierId = tokens.next();
        if (!cashierManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return null;
        }
        return cashierId;
    }

    private Integer getTicketId(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
            return null;
        }

        int ticketId = tokens.nextInt();
        if (!ticketManager.existId(ticketId)) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return null;
        }
        return ticketId;
    }
}
