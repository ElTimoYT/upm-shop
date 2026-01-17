package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractPeopleProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;

import java.util.ArrayList;
import java.util.Date;

public class OnlyServicesTicket extends TicketDTO {

    public OnlyServicesTicket(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.COMPANY);
    }

    @Override
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (!(productToAdd instanceof ServiceDTO)) return false;
        return super.addProduct(productToAdd, quantity);
    }
}
