package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

public interface TicketPrinter {
    String print(AbstractTicketDTO ticket);
}
