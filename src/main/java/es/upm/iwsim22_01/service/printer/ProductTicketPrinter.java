package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductTicketPrinter implements TicketPrinter {
    @Override
    public String print(AbstractTicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(ticket.getFormattedId()).append("\n");

        List<AbstractProductDTO> items = new ArrayList<>(ticket.getProducts());

        items.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));

        for (AbstractProductDTO product : items) {
            for (int i = 0; i < product.getAmount(); i++) {
                sb.append(product).append("\n");
            }
        }

        double totalProducts = AbstractTicketDTO.round2(ticket.totalProductsPrice());
        double totalDiscount = AbstractTicketDTO.round2(ticket.totalDiscount());
        double finalPrice = AbstractTicketDTO.round2(ticket.finalPrice());

        sb.append("\n");
        sb.append("Total price: ").append(String.format("%.2f", totalProducts)).append("\n");
        sb.append("Total discount: ").append(String.format("%.2f", totalDiscount)).append("\n");
        sb.append("Final Price: ").append(String.format("%.2f", finalPrice)).append("\n");

        return sb.toString();
    }
}
