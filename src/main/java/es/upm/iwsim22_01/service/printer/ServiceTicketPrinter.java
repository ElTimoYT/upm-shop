package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractPeopleProductDTO;
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
        items.removeIf(p -> !(p instanceof AbstractPeopleProductDTO)); // solo servicios

        items.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));

        for (AbstractProductDTO product : items) {
            AbstractPeopleProductDTO service = (AbstractPeopleProductDTO) product;
            sb.append(service.printTicketWithPeople()).append("\n");
        }

        return sb.toString();
    }
}
