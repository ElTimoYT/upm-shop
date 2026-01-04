package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.TicketDTO;

import java.util.Comparator;
import java.util.List;

public class CashierShowTicketCommandHandler implements CommandHandler {

    private static final String
            CASHIER_NOT_FOUND = "Cashier not found",
            CASHIER_SHOW_TICKETS_INCORRECT_USE = "Incorrect use: cash tickets <id>",
            CASHIER_SHOW_TICKETS_OK = "cash tickets: ok",
            CASHIER_SHOW_TICKETS_FAIL = "cash tickets: fail";

    private CashierService cashierManager;

    public CashierShowTicketCommandHandler(CashierService cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String id = tokens.next();
            if (!cashierManager.existsId(id)) {
                System.out.println(CASHIER_NOT_FOUND);
                return;
            }

            CashierDTO cashier = cashierManager.get(id);

            System.out.println("Tickets: ");
            List<TicketDTO> tickets = cashier.getTickets();

            if (tickets != null && !tickets.isEmpty()) {
                tickets.stream()
                        .sorted(Comparator.comparing(ticket -> ticket.getId()))
                        .forEach(t -> System.out.println("  " + t.getId() + "->" + t.getState()));
            }
            System.out.println(CASHIER_SHOW_TICKETS_OK);
        }catch (Exception e) {
            System.out.println(CASHIER_SHOW_TICKETS_INCORRECT_USE);
        }
    }
}
