package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.service.TicketService;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.NoSuchElementException;

public class TicketRemoveCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_REMOVE = "Incorrect use: ticket remove <ticketId> <cashId> <prodId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",
            ERROR_CASHIER_NOT_ASSIGNED = "Cashier is not assigned to this ticket",

            TICKET_REMOVAL_OK = "ticket remove: ok";

    private final TicketService ticketService;
    private final ProductService productService;
    private final CashierService cashierService;

    public TicketRemoveCommandHandler(TicketService ticketService, ProductService productService, CashierService cashierService) {
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

            int productId = tokens.nextInt();
            if (!productService.existsId(productId)) {
                System.out.println(ERROR_PRODUCT_NOT_FOUND);
                return;
            }

            AbstractProductDTO product = productService.get(productId);
            TicketDTO ticket = ticketService.get(ticketId);
            CashierDTO cashier = cashierService.get(cashierId);
            if (!cashier.getTickets().contains(ticket)) {
                System.out.println(ERROR_CASHIER_NOT_ASSIGNED);
                return;
            }

            ticket.removeProduct(product);
            ticketService.update(ticket);
            System.out.println(ticket.printTicket());

            System.out.println(TICKET_REMOVAL_OK);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
        }
    }
}
