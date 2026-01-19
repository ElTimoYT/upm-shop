package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;
import es.upm.iwsim22_01.service.printer.ServiceTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ticket que permite únicamente la adición de servicios y no admite productos.
 */
public class OnlyServicesTicket extends AbstractTicketDTO {
    private static final TicketPrinter TICKET_PRINTER = new ServiceTicketPrinter();

    /**
     * Crea un ticket vacío que solo admite servicios.
     *
     * @param id identificador del ticket
     */
    public OnlyServicesTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.ONLY_SERVICES);
    }

    /**
     * Crea un ticket de solo servicios con todos sus atributos inicializados.
     *
     * @param id identificador del ticket
     * @param initialDate fecha de creación del ticket
     * @param finalDate fecha de cierre del ticket
     * @param state estado actual del ticket
     * @param products lista inicial de servicios
     */
    public OnlyServicesTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.ONLY_SERVICES);
    }

    /**
     * Añade un servicio al ticket siempre que el producto sea de tipo servicio.
     *
     * @param productToAdd producto a añadir
     * @param quantity cantidad a añadir
     * @return true si el servicio se añade correctamente
     */
    @Override
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (!(productToAdd instanceof ServiceDTO)) return false;
        return super.addProduct(productToAdd, quantity);
    }

    /**
     * Devuelve la representación impresa del ticket de solo servicios.
     *
     * @return representación impresa del ticket
     */
    @Override
    public String printTicket() {
        return printTicket(TICKET_PRINTER);
    }
}
