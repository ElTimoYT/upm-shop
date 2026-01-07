package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.TicketService;

public class CashierRemoveCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER_REMOVE = "Incorrect use: cash remove <id>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CASHIER_WITH_CLIENTS = "Cannot remove a cashier with clients",

            CASHIER_REMOVE_OK = "cash remove: ok",
            CASHIER_REMOVE_FAIL = "cash remove: fail";

    private CashierService cashierService;
    private ClientService clientService;
    private TicketService ticketService;

    public CashierRemoveCommandHandler(CashierService cashierService, ClientService clientService, TicketService ticketService) {
        this.cashierService = cashierService;
        this.clientService = clientService;
        this.ticketService = ticketService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {

        String id = tokens.next();

        if (id == null) return;

        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_CASHIER_REMOVE);
            return;
        }

        if (!cashierService.existsId(id)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }
        CashierDTO cashierDTO = cashierService.get(id);


        if (clientService.getAll()
                .stream()
                .anyMatch(clientDTO -> clientDTO.getCashier().equals(cashierDTO))
        ) {
            System.out.println(ERROR_CASHIER_WITH_CLIENTS);
            return;
        }

        cashierDTO.getTickets().forEach(ticketDTO -> ticketService.remove(ticketDTO.getId()));

        cashierService.remove(id);
        System.out.println(cashierDTO);
        System.out.println(CASHIER_REMOVE_OK);
    }
}
