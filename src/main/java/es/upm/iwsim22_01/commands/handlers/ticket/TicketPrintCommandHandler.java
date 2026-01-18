package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.TicketService;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.NoSuchElementException;

public class TicketPrintCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_PRINT = "Incorrect use: ticket print <ticketId> <cashId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_TICKET_SERVICE_PRODUCT_INVALID = "One of the service products expiration date is invalid",
            ERROR_CASHIER_NOT_ASSIGNED = "Cashier is not assigned to this ticket",

            TICKET_PRINT_OK = "ticket print: ok";

    private final TicketService ticketService;
    private final CashierService cashierService;

    public TicketPrintCommandHandler(TicketService ticketService, CashierService cashierService) {
        this.ticketService = ticketService;
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

            TicketDTO ticket = ticketService.get(ticketId);
            CashierDTO cashier = cashierService.get(cashierId);
            if (!cashier.getTickets().contains(ticket)) {
                System.out.println(ERROR_CASHIER_NOT_ASSIGNED);
                return;
            }

            if (!ticket.areAllServiceProductsValid()) {
                System.out.println(ERROR_TICKET_SERVICE_PRODUCT_INVALID);
                return;
            }

            System.out.println(ticket.printTicket());
            ticket.closeTicket();
            ticketService.update(ticket);
            System.out.println(TICKET_PRINT_OK);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
        }
    }
}
