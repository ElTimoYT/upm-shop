package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractPeopleProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicesAndProductsTicket extends TicketDTO {
    public ServicesAndProductsTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.SERVICES_AND_PRODUCTS);
    }

    public ServicesAndProductsTicket(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.SERVICES_AND_PRODUCTS);
    }
}
