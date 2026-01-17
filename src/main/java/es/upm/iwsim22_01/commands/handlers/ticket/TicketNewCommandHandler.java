package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;
import es.upm.iwsim22_01.service.dto.user.CompanyDTO;
import es.upm.iwsim22_01.service.inventory.CashierInventory;
import es.upm.iwsim22_01.service.inventory.ClientInventory;
import es.upm.iwsim22_01.service.inventory.TicketInventory;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.printer.CombinedTicketPrinter;
import es.upm.iwsim22_01.service.printer.ProductTicketPrinter;
import es.upm.iwsim22_01.service.printer.ServiceTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

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
            TICKET_NEW_OK = "ticket new: ok",
            ERROR_TICKET_NOT_COMPANY_FOR_VISUALIZATION =
                    "Ticket is not company, only -p visualization is allowed";

    private final TicketInventory ticketService;
    private final CashierInventory cashierService;
    private final ClientInventory clientService;

    public TicketNewCommandHandler(TicketInventory ticketService, CashierInventory cashierService, ClientInventory clientService) {
        this.ticketService = ticketService;
        this.cashierService = cashierService;
        this.clientService = clientService;
    }
    private boolean isCompanyClient(ClientDTO client) {
        return client instanceof CompanyDTO;
    }
    private boolean isValidVisualizationForClient(ClientDTO client, String opt) {
        // default -p si no viene flag
        if (opt == null || opt.isBlank()) return true;

        // Normalizamos
        opt = opt.trim();

        // -p siempre permitido
        if (opt.equals("-p")) return true;

        // -s y -c solo si es empresa
        if (opt.equals("-s") || opt.equals("-c")) {
            return isCompanyClient(client);
        }
        return false;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            switch (tokens.getRemainingTokens()) {
                case 4:
                    newTicketCommandWithIdWithFlag(tokens);
                    break;
                case 3:
                    String token1 = tokens.next();
                    String token2 = tokens.next();
                    String token3 = tokens.next();
                    if (token3.equals("-p") || token3.equals("-s") || token3.equals("-c")) {
                        // Caso: ticket new <cashId> <userId> <flag>
                        newTicketCommandWithoutIdWithFlag(token1, token2, token3);
                    } else {
                        // Caso: ticket new <ticketId> <cashId> <userId>
                        int ticketId = Integer.parseInt(token1);
                        newTicketCommandWithIdWithoutFlag(ticketId, token2, token3);
                    }
                    break;
                case 2:
                    newTicketCommandWithoutIdWithoutFlag(tokens);
                    break;
                default:
                    System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
            }
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
    }

    private void newTicketCommandWithoutIdWithoutFlag(CommandTokens tokens) {

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

        TicketDTO ticket = ticketService.addTicketForClient(client);

        cashier.addTicket(ticket);
        System.out.println("cashierId token = " + cashierId);
        System.out.println("cashier DTO id = " + cashier.getId());
        System.out.println("cashierId token = " + clientId);
        System.out.println("cashier DTO id = " + client.getId());
        cashierService.update(cashier);


        client.addTicket(ticket);
        clientService.update(client);

        // Sin flag => -p
        System.out.println(new ProductTicketPrinter().print(ticket));
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithIdWithoutFlag(int id, String cashierId, String clientId) {
        int ticketId = id;
        if (ticketService.existsId(ticketId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }
        if (!ticketService.checkId(ticketId)) {
            System.out.println(ERROR_INVALID_ID_FORMAT);
            return;
        }
        if (!cashierService.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }
        if (!clientService.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        CashierDTO cashier = cashierService.get(cashierId);
        ClientDTO client = clientService.get(clientId);

        // Crea ticket según tipo de cliente, usando el ID dado
        TicketDTO ticket = ticketService.addTicketForClient(client, ticketId);

        cashier.addTicket(ticket);
        cashierService.update(cashier);

        client.addTicket(ticket);
        clientService.update(client);

        // Sin flag => -p
        System.out.println(new ProductTicketPrinter().print(ticket));
        System.out.println(TICKET_NEW_OK);
    }
    private void newTicketCommandWithoutIdWithFlag(String cashierId, String clientId, String opt) {
        if (!cashierService.existsId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }
        if (!clientService.existsId(clientId)) {
            System.out.println(ERROR_CLIENT_NOT_FOUND);
            return;
        }

        CashierDTO cashier = cashierService.get(cashierId);
        ClientDTO client = clientService.get(clientId);

        // si es USER, no se permite -s ni -c
        if (!isValidVisualizationForClient(client, opt)) {
            System.out.println(ERROR_TICKET_NOT_COMPANY_FOR_VISUALIZATION);
            return;
        }

        TicketDTO ticket = ticketService.addTicketForClient(client);

        cashier.addTicket(ticket);
        cashierService.update(cashier);


        System.out.println("clientId token = " + clientId);
        System.out.println("client DTO id = " + client.getId());

        client.addTicket(ticket);
        clientService.update(client);

        TicketPrinter printer = resolvePrinter(opt);
        System.out.println(printer.print(ticket));
        System.out.println(TICKET_NEW_OK);
    }
    private void newTicketCommandWithIdWithFlag(CommandTokens tokens) {
        int ticketId = tokens.nextInt();
        if (ticketService.existsId(ticketId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }
        if (!ticketService.checkId(ticketId)) {
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

        String opt = tokens.next(); // "-p" | "-s" | "-c"

        CashierDTO cashier = cashierService.get(cashierId);
        ClientDTO client = clientService.get(clientId);

        // si es USER, no se permite -s ni -c
        if (!isValidVisualizationForClient(client, opt)) {
            System.out.println(ERROR_TICKET_NOT_COMPANY_FOR_VISUALIZATION);
            return;
        }

        TicketDTO ticket = ticketService.addTicketForClient(client, ticketId);

        cashier.addTicket(ticket);
        cashierService.update(cashier);

        client.addTicket(ticket);
        clientService.update(client);

        TicketPrinter printer = resolvePrinter(opt);
        ticketService.setPrinterForTicket(ticket.getId(), printer);
        System.out.println(printer.print(ticket));
        System.out.println(TICKET_NEW_OK);
    }


    private TicketPrinter resolvePrinter(String opt) {
        return switch (opt) {
            case "-c" -> new CombinedTicketPrinter();
            case "-s" -> new ServiceTicketPrinter();
            case "-p" -> new ProductTicketPrinter();
            default -> new ProductTicketPrinter();
        };
    }


}
