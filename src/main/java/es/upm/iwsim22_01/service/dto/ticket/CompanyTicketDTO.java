package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.util.Date;
import java.util.List;

public class CompanyTicketDTO extends TicketDTO {

    public CompanyTicketDTO(int id) {
    super(id);
    setTicketType(TicketType.COMPANY);
}

    public CompanyTicketDTO(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products);
        setTicketType(TicketType.COMPANY);
    }
}
