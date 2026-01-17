package es.upm.iwsim22_01.service.printer;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.product.ProductService;
import es.upm.iwsim22_01.service.dto.ticket.CompanyTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CombinedTicketPrinter implements TicketPrinter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");

    private boolean isService(AbstractProductDTO p) {
        return p instanceof ProductService;
    }
    @Override
    public String print(TicketDTO ticket) {
        return printCompany(ticket);
    }

    private String printCompany(TicketDTO ticket) {
        StringBuilder sb = new StringBuilder();
        CompanyTicketDTO companyTicket = (CompanyTicketDTO) ticket;

        // Cabecera (no uso getFormattedCreationDate() porque no existe; uso initialDate)
        sb.append("Ticket: ")
                .append(ticket.getId())
                .append("-")
                .append(DATE_FORMAT.format(ticket.getInitialDate()))
                .append("\n");

        // Separar items
        List<AbstractProductDTO> services = new ArrayList<>();
        List<AbstractProductDTO> products = new ArrayList<>();

        for (AbstractProductDTO p : ticket.getProducts()) {
            if (isService(p)) services.add(p);
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
        if (companyTicket.isOnlyServicesTicket()) {
            return sb.toString();
        }

        double totalProducts = TicketDTO.round1(companyTicket.totalProductsPrice());
        double baseDiscount = TicketDTO.round1(companyTicket.baseProductDiscount());
        double extraServiceDiscount = TicketDTO.round1(companyTicket.extraServiceDiscount());
        double totalDiscount = TicketDTO.round1(companyTicket.totalDiscount());
        double finalPrice = TicketDTO.round1(companyTicket.finalPrice());

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
