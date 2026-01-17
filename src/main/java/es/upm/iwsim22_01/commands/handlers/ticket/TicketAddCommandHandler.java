package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.service.TicketService;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.dto.product.PersonalizableDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractPeopleProductDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;

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
            ERROR_PRODUCT_NOT_ALLOWED = "Product not allowed for this ticket",
            ERROR_PARAM_NOT_VALID = "Command param %s not valid, the command will ignore this param\n",

    TICKET_CLOSED = "Ticket closed",
            TICKET_ADD_OK = "ticket add: ok";


    private final TicketService ticketService;
    private final ProductService productService;
    private final CashierService cashierService;

    public TicketAddCommandHandler(TicketService ticketService, ProductService productService, CashierService cashierService) {
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

            String productId = tokens.next();
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
                if (product instanceof AbstractPeopleProductDTO peopleProduct) {
                    if (!peopleProduct.isValid()) {
                        System.out.println(ERROR_INVALID_DATE);
                        return;
                    }
                    AbstractPeopleProductDTO newPeopleProduct = (AbstractPeopleProductDTO) peopleProduct.clone();
                    newPeopleProduct.setParticipantsAmount(amount + peopleProduct.getParticipantsAmount());
                    added = ticket.addProduct(newPeopleProduct, amount);
                }else{
                    if (tokens.hasNext()) {
                        if (product instanceof PersonalizableDTO personalizableProduct) {
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
                            PersonalizableDTO personalizableToAdd = personalizableProduct.clone();
                            personalizableToAdd.setLines(lines);
                            added = ticket.addProduct(personalizableToAdd, amount);
                        } else {
                            System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                            return;
                        }
                    } else {
                        added = ticket.addProduct(product.clone(), amount);
                    }
                }

                if (!added) {
                    System.out.println(ERROR_PRODUCT_NOT_ALLOWED);
                    return;
                }

                ticketService.update(ticket);
                System.out.println(ticket.printTicket());
                System.out.println(TICKET_ADD_OK);
            } else{
                System.out.println(TICKET_CLOSED);
            }


        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
        }
    }

}
