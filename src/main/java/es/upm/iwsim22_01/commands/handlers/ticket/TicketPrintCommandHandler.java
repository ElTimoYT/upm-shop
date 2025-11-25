package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.commands.predicates.CheckIdInManagerPredicate;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;

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
        try {
            OptionalInt ticketId = tokens.nextInt(new CheckIdInManagerPredicate<>(ticketManager));
            if (ticketId.isEmpty()) {
                System.out.println(ERROR_TICKET_NOT_FOUND);
                return;
            }

            Optional<String> cashierId = tokens.next(new CheckIdInManagerPredicate<>(cashierManager));
            if (cashierId.isEmpty()) {
                System.out.println(ERROR_CASHIER_NOT_FOUND);
                return;
            }

            Ticket ticket = ticketManager.get(ticketId.getAsInt());
            System.out.println(ticket.toString());
            ticket.closeTicket();

            System.out.println(TICKET_PRINT_OK);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
        }
    }
}
