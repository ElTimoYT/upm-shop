package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Impresor de tickets que muestra únicamente servicios incluidos en el ticket.
 */
public class ServiceTicketPrinter implements  TicketPrinter {

    /**
     * Genera la representación textual de un ticket que contiene solo servicios.
     *
     * @param ticket ticket a imprimir
     * @return representación textual del ticket
     */
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
