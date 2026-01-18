package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.printer.CombinedTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicesAndProductsTicket extends AbstractTicketDTO {
    private static final TicketPrinter TICKET_PRINTER = new CombinedTicketPrinter();

    public ServicesAndProductsTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.SERVICES_AND_PRODUCTS);
    }

    public ServicesAndProductsTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.SERVICES_AND_PRODUCTS);
    }

    @Override
    public String printTicket() {
        return printTicket(TICKET_PRINTER);
    }
}
