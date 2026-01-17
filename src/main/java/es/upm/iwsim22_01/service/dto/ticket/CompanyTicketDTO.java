package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyTicketDTO extends TicketDTO {

    public CompanyTicketDTO(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.COMPANY);
    }
}
