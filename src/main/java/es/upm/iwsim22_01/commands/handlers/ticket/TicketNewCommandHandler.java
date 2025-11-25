package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.commands.predicates.CheckIdInManagerPredicate;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;

public class TicketNewCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<ticketId>] <cashId> <userId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_INVALID_ID = "Invalid id",

            TICKET_NEW_OK = "ticket new: ok";

    private final TicketManager ticketManager;
    private final CashierManager cashierManager;
    private final ClientManager clientManager;

    public TicketNewCommandHandler(TicketManager ticketManager, CashierManager cashierManager, ClientManager clientManager) {
        this.ticketManager = ticketManager;
        this.cashierManager = cashierManager;
        this.clientManager = clientManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            switch (tokens.getRemainingTokens()) {
                case 3:
                    newTicketCommandWithId(tokens);
                    break;
                case 2:
                    newTicketCommandWithoutId(tokens);
                    break;
                default:
                    System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            }
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
    }

    private void newTicketCommandWithoutId(CommandTokens tokens) {
        Optional<String> cashierId = tokens.next(new CheckIdInManagerPredicate<>(cashierManager));
        if (cashierId.isEmpty()) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        Optional<String> clientId = tokens.next(new CheckIdInManagerPredicate<>(clientManager));
        if (clientId.isEmpty()) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        Ticket ticket = ticketManager.addTicket();
        cashierManager.get(cashierId.get()).addTicket(ticket);
        clientManager.get(clientId.get()).addTicket(ticket);

        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(CommandTokens tokens) {
        OptionalInt ticketId = tokens.nextInt(new CheckIdInManagerPredicate<>(ticketManager).negate());
        if (ticketId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        Optional<String> cashierId = tokens.next(new CheckIdInManagerPredicate<>(cashierManager));
        if (cashierId.isEmpty()) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        Optional<String> clientId = tokens.next(new CheckIdInManagerPredicate<>(clientManager));
        if (clientId.isEmpty()) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        Ticket ticket = ticketManager.addTicket(ticketId.getAsInt());
        cashierManager.get(cashierId.get()).addTicket(ticket);
        clientManager.get(clientId.get()).addTicket(ticket);

        System.out.println(TICKET_NEW_OK);
    }
}
