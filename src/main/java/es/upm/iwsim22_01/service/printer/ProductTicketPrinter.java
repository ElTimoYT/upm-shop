package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductTicketPrinter implements TicketPrinter {
    @Override
    public String print(TicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(ticket.getFormattedId()).append("\n");

        List<AbstractProductDTO> items = new ArrayList<>(ticket.getProducts());
        items.removeIf(p -> p instanceof AbstractTypeDTO); // quitamos servicios

        items.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));

        for (AbstractProductDTO product : items) {
            for (int i = 0; i < product.getAmount(); i++) {
                sb.append(product).append("\n");
            }
        }

        return sb.toString();
    }
}
