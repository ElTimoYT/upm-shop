package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

/**
 * Interfaz que define la capacidad de imprimir un ticket en formato textual.
 */
public interface TicketPrinter {

    /**
     * Genera la representación textual de un ticket.
     *
     * @param ticket ticket a imprimir
     * @return representación textual del ticket
     */
    String print(AbstractTicketDTO ticket);
}
