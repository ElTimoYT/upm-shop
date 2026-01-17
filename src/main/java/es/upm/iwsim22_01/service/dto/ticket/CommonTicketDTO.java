package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.product.PersonalizableProductDTO;
import es.upm.iwsim22_01.service.dto.product.ProductService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonTicketDTO extends TicketDTO {

    public CommonTicketDTO(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.COMMON);
    }

    public CommonTicketDTO(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.COMMON);
    }
    @Override
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (productToAdd instanceof ProductService) return false; // common no admite servicios
        return super.addProduct(productToAdd, quantity);
    }

    @Override
    public boolean addProduct(PersonalizableProductDTO personalizableProduct, int quantity, String[] lines) {
        return super.addProduct(personalizableProduct, quantity, lines);
    }


}
