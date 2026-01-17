package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.data.models.Product;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.product.ProductService;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceTicketPrinter implements  TicketPrinter {
    @Override
    public String print(TicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(ticket.getFormattedId()).append("\n");

        List<AbstractProductDTO> items = new ArrayList<>(ticket.getProducts());
        items.removeIf(p -> !(p instanceof ProductService));

        if (items.isEmpty()) {
            sb.append("Services Included:\n  None\n");
            return sb.toString();
        }

        items.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));

        sb.append("Services Included:\n");
        for (AbstractProductDTO product : items) {
            ProductService service = (ProductService) product;
            sb.append("  ").append(service).append("\n");
        }

        return sb.toString();
    }
}
