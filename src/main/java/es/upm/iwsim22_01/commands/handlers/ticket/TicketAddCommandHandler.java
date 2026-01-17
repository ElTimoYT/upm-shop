package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.product.ProductService;
import es.upm.iwsim22_01.service.inventory.CashierInventory;
import es.upm.iwsim22_01.service.inventory.ProductInventory;
import es.upm.iwsim22_01.service.inventory.TicketInventory;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.dto.product.PersonalizableProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TicketAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]",
            ERROR_PRODUCT_IS_NO_PERSONALIZABLE = "Product is not personalizable",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",
            ERROR_INVALID_DATE = "Date of the product already passed",
            ERROR_CASHIER_NOT_ASSIGNED = "Cashier is not assigned to this ticket",
            ERROR_PRODUCT_NOT_ALLOWED = "Product not allowed for this ticket type",
            ERROR_PARAM_NOT_VALID = "Command param %s not valid, the command will ignore this param\n",

    TICKET_CLOSED = "Ticket closed",
            TICKET_ADD_OK = "ticket add: ok";


    private final TicketInventory ticketService;
    private final ProductInventory productService;
    private final CashierInventory cashierService;

    public TicketAddCommandHandler(TicketInventory ticketService, ProductInventory productService, CashierInventory cashierService) {
        this.ticketService = ticketService;
        this.productService = productService;
        this.cashierService = cashierService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            int ticketId = tokens.nextInt();
            if (!ticketService.existsId(ticketId)) {
                System.out.println(ERROR_TICKET_NOT_FOUND);
                return;
            }

            String cashierId = tokens.next();
            if (!cashierService.existsId(cashierId)) {
                System.out.println(ERROR_CASHIER_NOT_FOUND);
                return;
            }
            String prodToken = tokens.next(); // "5" o "1S"
            boolean isNumericProduct = prodToken.chars().allMatch(Character::isDigit);
            boolean isServiceCode =
                    !isNumericProduct
                            && prodToken.length() > 1
                            && (prodToken.endsWith("S") || prodToken.endsWith("s"))
                            && prodToken.substring(0, prodToken.length() - 1).chars().allMatch(Character::isDigit);

            if (isNumericProduct) {
                int productId = Integer.parseInt(prodToken);

                if (!productService.existsId(productId)) {
                    System.out.println(ERROR_PRODUCT_NOT_FOUND);
                    return;
                }
                int amount = tokens.nextInt();
                if (amount <= 0 || amount > TicketDTO.MAX_PRODUCTS) {
                    System.out.println(ERROR_INVALID_AMOUNT);
                    return;
                }

                AbstractProductDTO product = productService.get(productId);
                TicketDTO ticket = ticketService.get(ticketId);
                CashierDTO cashier = cashierService.get(cashierId);
                if (!cashier.getTickets().contains(ticket)) {
                    System.out.println(ERROR_CASHIER_NOT_ASSIGNED);
                    return;
                }

                if(ticket.getState() != TicketDTO.TicketState.CLOSED){
                    boolean added;
                    if (product instanceof AbstractTypeDTO productService) {
                        if (!productService.isValid()) {
                            System.out.println(ERROR_INVALID_DATE);
                            return;
                        }
                        productService.setParticipantsAmount(amount + productService.getParticipantsAmount());
                        added = ticket.addProduct(productService, amount);

                    }else{
                        if (tokens.hasNext()) {
                            if (product instanceof PersonalizableProductDTO personalizableProduct) {
                                List<String> personalization = new ArrayList<>();
                                while (tokens.hasNext()) {
                                    String token = tokens.next();
                                    if (token.startsWith("--p")) {
                                        String text = token.substring(3).trim();
                                        if (!text.isEmpty()) {
                                            personalization.add(text);
                                        }
                                    } else {
                                        System.out.printf(ERROR_PARAM_NOT_VALID, token);
                                    }
                                }
                                String[] lines = personalization.toArray(new String[0]);
                                added = ticket.addProduct(personalizableProduct, amount, lines);
                            } else {
                                System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                                return;
                            }
                        } else {
                            added = ticket.addProduct(product, amount);
                        }
                    }

                    if (!added) {
                        System.out.println(ERROR_PRODUCT_NOT_ALLOWED);
                        return;
                    }

                    ticketService.update(ticket);
                    TicketPrinter print = ticketService.getPrinterForTicket(ticketId);
                    System.out.println(print.print(ticket));
                    System.out.println(TICKET_ADD_OK);
                } else{
                    System.out.println(TICKET_CLOSED);
                }

                return; // importante: no seguir al bloque de servicio
            }
            if (isServiceCode) {
                int serviceId = Integer.parseInt(prodToken.substring(0, prodToken.length() - 1));

                if (!productService.existsServiceId(serviceId)) {
                    System.out.println(ERROR_PRODUCT_NOT_FOUND);
                    return;
                }

                // amount para servicios: por defecto 1, o si quieres forzarlo a que venga en el comando, cambia esto

                AbstractProductDTO serviceProduct = productService.getServiceDto(serviceId);
                TicketDTO ticket = ticketService.get(ticketId);
                CashierDTO cashier = cashierService.get(cashierId);
                if (!cashier.getTickets().contains(ticket)) {
                    System.out.println(ERROR_CASHIER_NOT_ASSIGNED);
                    return;
                }

                if (ticket.getState() == TicketDTO.TicketState.CLOSED) {
                    System.out.println(TICKET_CLOSED);
                    return;
                }

                // Si tu ProductService tiene fecha de expiración / validación, hazlo aquí:
                if (serviceProduct instanceof ProductService ps) {
                    if (!ps.isValid()) {
                        System.out.println(ERROR_INVALID_DATE);
                        return;
                    }
                }

                boolean added = ticket.addProduct(serviceProduct,1);

                if (!added) {
                    System.out.println(ERROR_PRODUCT_NOT_ALLOWED);
                    return;
                }

                ticketService.update(ticket);
                TicketPrinter print = ticketService.getPrinterForTicket(ticketId);
                System.out.println(print.print(ticket));
                System.out.println(TICKET_ADD_OK);
                return;
            }

            // Si no era ni numérico ni "1S"
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);

        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
        }
    }


}
