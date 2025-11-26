package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Comparator;
import java.util.List;

public class TicketListCommandHandler implements CommandHandler {
    private static final String
            TICKET_LIST_OK = "ticket list: ok",
            TICKET_LIST = "Ticket List:";

    private final TicketManager ticketManager;

    public TicketListCommandHandler(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        List<Ticket> allTickets = ticketManager.getAll();
        allTickets.sort(Comparator.comparing(Ticket::getInitialDate));

        System.out.println(TICKET_LIST);

        for (Ticket ticket : allTickets) {
            String id = ticket.getFormattedId();
            String state = String.valueOf(ticket.getState());
            System.out.println("  " + id + " -> " + state);
        }
        System.out.println(TICKET_LIST_OK);
    }
}
