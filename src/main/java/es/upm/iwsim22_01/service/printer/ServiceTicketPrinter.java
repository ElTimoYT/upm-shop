package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceTicketPrinter implements  TicketPrinter {
    @Override
    public String print(AbstractTicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(ticket.getFormattedId()).append("\n");

        List<AbstractProductDTO> items = new ArrayList<>(ticket.getProducts());

        if (items.isEmpty()) {
            sb.append("Services Included:\n  None\n");
            return sb.toString();
        }

        sb.append("Services Included:\n");
        for (AbstractProductDTO product : items) {
            sb.append(product).append("\n");
        }

        return sb.toString();
    }
}
