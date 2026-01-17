package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

public interface TicketPrinter {
    String print(TicketDTO ticket);
}
