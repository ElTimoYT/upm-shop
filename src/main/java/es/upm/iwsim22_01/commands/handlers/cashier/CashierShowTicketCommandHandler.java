package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.user.Cashier;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CashierShowTicketCommandHandler implements CommandHandler {

    private static final String
            CASHIER_NOT_FOUND = "Cashier not found",
            CASHIER_SHOW_TICKETS_INCORRECT_USE = "Incorrect use: cash tickets <id>",
            CASHIER_SHOW_TICKETS_OK = "cash tickets: ok",
            CASHIER_SHOW_TICKETS_FAIL = "cash tickets: fail";

    private CashierManager cashierManager;

    public CashierShowTicketCommandHandler(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String id = tokens.next();
            if (!cashierManager.existId(id)) {
                System.out.println(CASHIER_NOT_FOUND);
                return;
            }

            Cashier cashier = cashierManager.get(id);

            System.out.println("Tickets: ");
            List<Ticket> tickets = cashier.getTickets();

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
