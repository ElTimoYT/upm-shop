package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.TicketInventory;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.Comparator;
import java.util.List;

public class TicketListCommandHandler implements CommandHandler {
    private static final String
            TICKET_LIST_OK = "ticket list: ok",
            TICKET_LIST = "Ticket List:";

    private final TicketInventory ticketService;

    public TicketListCommandHandler(TicketInventory ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        List<TicketDTO> allTickets = ticketService.getAll();
        allTickets.sort(Comparator.comparing(TicketDTO::getInitialDate));

        System.out.println(TICKET_LIST);

        for (TicketDTO ticket : allTickets) {
            String id = ticket.getFormattedId();
            String state = String.valueOf(ticket.getState());
            System.out.println("  " + id + " -> " + state);
        }
        System.out.println(TICKET_LIST_OK);
    }
}
