package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.TicketService;
import es.upm.iwsim22_01.service.dto.TicketDTO;

import java.util.NoSuchElementException;

public class TicketNewCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<ticketId>] <cashId> <userId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_INVALID_ID = "Invalid id",
            ERROR_INVALID_ID_FORMAT = "Ticket id format is invalid",

            TICKET = "Ticket: ",
            NEW_TICKET_DATA = """
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                    """,
            TICKET_NEW_OK = "ticket new: ok";

    private final TicketService ticketManager;
    private final CashierService cashierManager;
    private final ClientService clientManager;

    public TicketNewCommandHandler(TicketService ticketManager, CashierService cashierManager, ClientService clientManager) {
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
        String cashierId = tokens.next();
        if (!cashierManager.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        String clientId = tokens.next();
        if (!clientManager.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        TicketDTO ticket = ticketManager.addTicket();
        cashierManager.get(cashierId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);
        System.out.println("Ticket: " + ticket);
        System.out.println("  Total price: 0.0");
        System.out.println("  Total discount: 0.0");
        System.out.println("  Final Price: 0.0");
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(CommandTokens tokens) {
        int ticketId = tokens.nextInt();
        if (ticketManager.existsId(ticketId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }
        if (!ticketManager.checkId(ticketId)){
            System.out.println(ERROR_INVALID_ID_FORMAT);
            return;
        }

        String cashierId = tokens.next();
        if (!cashierManager.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        String clientId = tokens.next();
        if (!clientManager.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        TicketDTO ticket = ticketManager.addTicket(ticketId);
        cashierManager.get(cashierId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);
        System.out.println(TICKET + ticket.getFormattedId());
        System.out.println(NEW_TICKET_DATA);
        System.out.println(TICKET_NEW_OK);
    }
}
