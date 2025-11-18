package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

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
    }

    private void newTicketCommandWithoutId(CommandTokens tokens) {
        String cashierId = getCashierId(tokens);
        if (cashierId == null) return;

        String clientId = getClientId(tokens);
        if (clientId == null) return;

        Ticket ticket = ticketManager.addTicket();
        cashierManager.get(cashierId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);

        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(CommandTokens tokens) {
        Integer ticketId = getTicketNewId(tokens);
        if (ticketId == null) return;

        String cashierId = getCashierId(tokens);
        if (cashierId == null) return;

        String clientId = getClientId(tokens);
        if (clientId == null) return;

        Ticket ticket = ticketManager.addTicket(ticketId);
        cashierManager.get(cashierId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);

        System.out.println(TICKET_NEW_OK);
    }

    private String getCashierId(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            return null;
        }

        String cashierId = tokens.next();
        if (!cashierManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return null;
        }
        return cashierId;
    }

    private String getClientId(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            return null;
        }

        String clientId = tokens.next();
        if (!clientManager.existId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return null;
        }
        return clientId;
    }

    private Integer getTicketNewId(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            return null;
        }

        int ticketId = tokens.nextInt();
        if (ticketManager.existId(ticketId)) {
            System.out.println(ERROR_INVALID_ID);
            return null;
        }
        return ticketId;
    }
}
