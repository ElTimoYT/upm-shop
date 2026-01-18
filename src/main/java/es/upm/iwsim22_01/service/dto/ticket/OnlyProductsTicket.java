package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;
import es.upm.iwsim22_01.service.printer.ProductTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnlyProductsTicket extends AbstractTicketDTO {
    private static final TicketPrinter TICKET_PRINTER = new ProductTicketPrinter();

    public OnlyProductsTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.ONLY_PRODUCTS);
    }

    public OnlyProductsTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.ONLY_PRODUCTS);
    }

    @Override
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (productToAdd instanceof ServiceDTO) return false;
        return super.addProduct(productToAdd, quantity);
    }

    @Override
    public String printTicket() {
        return printTicket(TICKET_PRINTER);
    }
}
