package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;
import es.upm.iwsim22_01.service.dto.user.CompanyDTO;
import es.upm.iwsim22_01.service.printer.CombinedTicketPrinter;
import es.upm.iwsim22_01.service.printer.ProductTicketPrinter;
import es.upm.iwsim22_01.service.printer.ServiceTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.TicketService;

public class TicketNewCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_TICKET_NEW =
            "Incorrect use: ticket new [<ticketId>] <cashId> <userId> -[p|s|c]",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_INVALID_ID = "Invalid id",
            ERROR_INVALID_ID_FORMAT = "Ticket id format is invalid",
            ERROR_TICKET_NOT_COMPANY_FOR_VISUALIZATION =
                    "Ticket is not company, only -p option is allowed",
            TICKET_NEW_OK = "ticket new: ok";

    private final TicketService ticketService;
    private final CashierService cashierService;
    private final ClientService clientService;

    public TicketNewCommandHandler(
            TicketService ticketService,
            CashierService cashierService,
            ClientService clientService) {
        this.ticketService = ticketService;
        this.cashierService = cashierService;
        this.clientService = clientService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            Integer ticketId = null;
            String flag = "-p";

            if (tokens.hasNextInt()) {
                ticketId = tokens.nextInt();
                if (ticketService.existsId(ticketId)) {
                    System.out.println(ERROR_INVALID_ID);
                    return;
                }
                if (!ticketService.checkId(ticketId)) {
                    System.out.println(ERROR_INVALID_ID_FORMAT);
                    return;
                }
            }

            String cashierId = tokens.next();
            if (!cashierService.existsId(cashierId)) {
                System.out.println(ERROR_CASHIER_NOT_FOUND);
                return;
            }
            CashierDTO cashier = cashierService.get(cashierId);

            String clientId = tokens.next();
            if (!clientService.existsId(clientId)) {
                System.out.println(ERROR_CLIENT_NOT_FOUND);
                return;
            }
            ClientDTO client = clientService.get(clientId);

            if (tokens.getRemainingTokens() == 1) {
                flag = tokens.next();
            }

            if (!isValidForClient(client, flag)) {
                System.out.println(ERROR_TICKET_NOT_COMPANY_FOR_VISUALIZATION);
                return;
            }

            AbstractTicketDTO ticket;
            switch (flag) {
                case "-p":
                    ticket = ticketId == null ? ticketService.addOnlyProductsTicket() : ticketService.addOnlyProductsTicket(ticketId);
                    break;
                case "-s":
                    ticket = ticketId == null ? ticketService.addOnlyServicesTicket() : ticketService.addOnlyServicesTicket(ticketId);
                    break;
                case "-c":
                    ticket = ticketId == null ? ticketService.addServicesAndProductsTicket() : ticketService.addServicesAndProductsTicket(ticketId);
                    break;
                default:
                    System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
                    return;
            }

            cashier.addTicket(ticket);
            cashierService.update(cashier);

            client.addTicket(ticket);
            clientService.update(client);

            System.out.println(ticket.printTicket());
            System.out.println(TICKET_NEW_OK);

        } catch (Exception e) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
    }

    private boolean isValidForClient(ClientDTO client, String flag) {
        if (flag.equals("-p")) return true;
        return client instanceof CompanyDTO;
    }

    private boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
