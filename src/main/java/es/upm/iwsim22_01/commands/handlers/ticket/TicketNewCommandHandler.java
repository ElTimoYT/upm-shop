package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.data.models.Client;
import es.upm.iwsim22_01.service.dto.ticket.CommonTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.CompanyTicketDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.TicketService;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.NoSuchElementException;

public class TicketNewCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<ticketId>] <cashId> <userId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_INVALID_ID = "Invalid id",
            ERROR_INVALID_ID_FORMAT = "Ticket id format is invalid",

    TICKET = "Ticket: ",
            NEW_TICKET_DATA = """
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                    """,
            TICKET_NEW_OK = "ticket new: ok";

    private final TicketService ticketService;
    private final CashierService cashierService;
    private final ClientService clientService;

    public TicketNewCommandHandler(TicketService ticketService, CashierService cashierService, ClientService clientService) {
        this.ticketService = ticketService;
        this.cashierService = cashierService;
        this.clientService = clientService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            switch (tokens.getRemainingTokens()) {
                case 3:
                    newTicketCommandWithId(tokens);
                    break;
                case 2:
                    newTicketCommandWithoutId(tokens);
                    break;
                default:
                    System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            }
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
    }

    private void newTicketCommandWithoutId(CommandTokens tokens) {
        String cashierId = tokens.next();
        if (!cashierService.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        String clientId = tokens.next();
        if (!clientService.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        // Decidimos el tipo de ticket en función del identificador del cliente.
        // (En el enunciado se ha simplificado la jerarquía de clientes, así que
        // inferimos si es empresa/particular por el patrón del ID).
        ClientDTO client = clientService.get(clientId);
        TicketDTO ticket = createTicketByClientType(client, ticketService.createNewId());
        CashierDTO cashier = cashierService.get(cashierId);
        cashier.addTicket(ticket);
        cashierService.update(cashier);
        client.addTicket(ticket);
        clientService.update(client);


        System.out.println("Ticket: " + ticket);
        System.out.println("  Total price: 0.0");
        System.out.println("  Total discount: 0.0");
        System.out.println("  Final Price: 0.0");
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(CommandTokens tokens) {
        int ticketId = tokens.nextInt();
        if (ticketService.existsId(ticketId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }
        if (!ticketService.checkId(ticketId)){
            System.out.println(ERROR_INVALID_ID_FORMAT);
            return;
        }

        String cashierId = tokens.next();
        if (!cashierService.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        String clientId = tokens.next();
        if (!clientService.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        CashierDTO cashier = cashierService.get(cashierId);
        ClientDTO client = clientService.get(clientId);

        // Decide el tipo de ticket según el tipo de cliente
        TicketDTO ticket = createTicketByClientType(client, ticketId);

        cashier.addTicket(ticket);
        cashierService.update(cashier);

        client.addTicket(ticket);
        clientService.update(client);

        System.out.println(TICKET + ticket.getFormattedId());
        System.out.println(NEW_TICKET_DATA);
        System.out.println(TICKET_NEW_OK);
    }

    /**
     * Crea un ticket común o de compañía según el tipo de cliente.
     * @param client   El cliente para el cual se crea el ticket.
     * @param ticketId El identificador del ticket.
     * @return Un TicketDTO del tipo adecuado.
     */
    private TicketDTO createTicketByClientType(ClientDTO client, int ticketId) {
        return client.getClientType() == Client.ClientType.COMPANY
                ? new CompanyTicketDTO(ticketId)
                : new CommonTicketDTO(ticketId);
    }
}
