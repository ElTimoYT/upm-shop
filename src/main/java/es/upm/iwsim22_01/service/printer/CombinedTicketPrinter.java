package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

public class CombinedTicketPrinter implements TicketPrinter {
    @Override
    public String print(TicketDTO ticket) {
        return ticket.printTicket();
    }
}
