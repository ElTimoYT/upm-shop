package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.printer.CombinedTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ticket que permite la combinación de productos y servicios en un mismo ticket.
 */
public class ServicesAndProductsTicket extends AbstractTicketDTO {
    private static final TicketPrinter TICKET_PRINTER = new CombinedTicketPrinter();

    /**
     * Crea un ticket vacío que admite productos y servicios.
     *
     * @param id identificador del ticket
     */
    public ServicesAndProductsTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.SERVICES_AND_PRODUCTS);
    }

    /**
     * Crea un ticket combinado con todos sus atributos inicializados.
     *
     * @param id identificador del ticket
     * @param initialDate fecha de creación del ticket
     * @param finalDate fecha de cierre del ticket
     * @param state estado actual del ticket
     * @param products lista inicial de productos y servicios
     */
    public ServicesAndProductsTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.SERVICES_AND_PRODUCTS);
    }

    /**
     * Devuelve la representación impresa del ticket combinado.
     *
     * @return representación impresa del ticket
     */
    @Override
    public String printTicket() {
        return printTicket(TICKET_PRINTER);
    }
}
