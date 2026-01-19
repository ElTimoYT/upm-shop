package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;
import es.upm.iwsim22_01.service.printer.ProductTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ticket que permite únicamente la adición de productos y no admite servicios.
 */
public class OnlyProductsTicket extends AbstractTicketDTO {
    private static final TicketPrinter TICKET_PRINTER = new ProductTicketPrinter();

    /**
     * Crea un ticket vacío que solo admite productos.
     *
     * @param id identificador del ticket
     */
    public OnlyProductsTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.ONLY_PRODUCTS);
    }

    /**
     * Crea un ticket de solo productos con todos sus atributos inicializados.
     *
     * @param id identificador del ticket
     * @param initialDate fecha de creación del ticket
     * @param finalDate fecha de cierre del ticket
     * @param state estado actual del ticket
     * @param products lista inicial de productos
     */
    public OnlyProductsTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.ONLY_PRODUCTS);
    }

    /**
     * Añade un producto al ticket siempre que no sea un servicio.
     *
     * @param productToAdd producto a añadir
     * @param quantity cantidad a añadir
     * @return true si el producto se añade correctamente
     */
    @Override
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (productToAdd instanceof ServiceDTO) return false;
        return super.addProduct(productToAdd, quantity);
    }

    /**
     * Devuelve la representación impresa del ticket de solo productos.
     *
     * @return representación impresa del ticket
     */
    @Override
    public String printTicket() {
        return printTicket(TICKET_PRINTER);
    }
}
