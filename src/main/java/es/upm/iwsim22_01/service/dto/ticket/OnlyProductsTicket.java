package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractPeopleProductDTO;
import es.upm.iwsim22_01.service.dto.product.ProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnlyProductsTicket extends TicketDTO {

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
}
