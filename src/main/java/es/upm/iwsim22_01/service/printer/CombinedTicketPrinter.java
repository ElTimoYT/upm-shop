package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CombinedTicketPrinter implements TicketPrinter {
    @Override
    public String print(AbstractTicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        // Cabecera (no uso getFormattedCreationDate() porque no existe; uso initialDate)
        sb.append("Ticket: ")
                .append(ticket.getId())
                .append("-")
                .append(AbstractTicketDTO.DATE_FORMAT.format(ticket.getInitialDate()))
                .append("\n");

        List<AbstractProductDTO> services = new ArrayList<>();
        List<AbstractProductDTO> products = new ArrayList<>();

        for (AbstractProductDTO p : ticket.getProducts()) {
            if (p instanceof ServiceDTO) services.add(p);
            else products.add(p);
        }

        services.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));
        products.sort(Comparator.comparing(AbstractProductDTO::getName, String.CASE_INSENSITIVE_ORDER));

        sb.append("Services Included:\n");
        if (services.isEmpty()) {
            sb.append("  None\n");
        } else {
            for (AbstractProductDTO p : services) {
                sb.append("  ").append(p).append("\n");
            }
        }
        sb.append("Products Included:\n");
        if (products.isEmpty()) {
            sb.append("  None\n");
        } else {
            for (AbstractProductDTO p : products) {
                sb.append("  ").append(p).append("\n");
            }
        }

        double totalProducts = AbstractTicketDTO.round2(ticket.totalProductsPrice());
        double baseDiscount = AbstractTicketDTO.round2(ticket.baseProductDiscount());
        double extraServiceDiscount = AbstractTicketDTO.round2(ticket.extraServiceDiscount());
        double totalDiscount = AbstractTicketDTO.round2(ticket.totalDiscount());
        double finalPrice = AbstractTicketDTO.round2(ticket.finalPrice());

        sb.append("\n");
        sb.append("Total price: ").append(String.format("%.2f", totalProducts)).append("\n");

        if (baseDiscount > 0) {
            sb.append("Total base discount: ").append(String.format("%.2f", baseDiscount)).append("\n");
        }

        if (extraServiceDiscount > 0) {
            sb.append("Extra Discount from services: ")
                    .append(String.format("%.2f", extraServiceDiscount))
                    .append("\n");
        }

        sb.append("Total discount: ").append(String.format("%.2f", totalDiscount)).append("\n");
        sb.append("Final Price: ").append(String.format("%.2f", finalPrice)).append("\n");

        return sb.toString();
    }
}
