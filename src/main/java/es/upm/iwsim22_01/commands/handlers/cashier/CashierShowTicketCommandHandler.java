package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class CashierShowTicketCommandHandler implements CommandHandler {

    private static final String
            CASHIER_SHOW_TICKETS_INCORRECT_USE = "Incorrect use: cashier tickets <id>",
            CASHIER_SHOW_TICKETS_OK = "cash tickets: ok",
            CASHIER_SHOW_TICKETS_FAIL = "cash tickets: fail";

    private CashierManager cashierManager;

    public CashierShowTicketCommandHandler(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String id = tokens.nextAsStringId(cashierManager, false, CASHIER_SHOW_TICKETS_INCORRECT_USE, CASHIER_SHOW_TICKETS_INCORRECT_USE);
            if (tokens.hasNext()) {
                System.out.println(CASHIER_SHOW_TICKETS_INCORRECT_USE);
                return;
            }
            Optional<Cashier> optionalCashier = Optional.ofNullable(cashierManager.get(id));
            optionalCashier.ifPresentOrElse(
                    cashier -> {
                        System.out.println("Tickets: ");
                        Collection<Ticket> tickets = cashier.getTickets();

                        if (tickets != null && !tickets.isEmpty()) {
                            tickets.stream()
                                    .sorted(Comparator.comparing(Ticket::getId))
                                    .forEach(t -> System.out.println("  " + t.getId() + "->" + t.getState()));
                        }
                        System.out.println(CASHIER_SHOW_TICKETS_OK);
                    },
                    () -> System.out.println(CASHIER_SHOW_TICKETS_FAIL)
            );
        }catch (Exception e) {
            System.out.println(CASHIER_SHOW_TICKETS_FAIL);
        }
    }
}
