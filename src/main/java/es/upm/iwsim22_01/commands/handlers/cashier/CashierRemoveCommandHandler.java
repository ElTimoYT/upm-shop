package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.TicketService;

public class CashierRemoveCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER_REMOVE = "Incorrect use: cash remove <id>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",

            CASHIER_REMOVE_OK = "cash remove: ok",
            CASHIER_REMOVE_FAIL = "cash remove: fail";

    private CashierService cashierManager;
    private TicketService ticketManager;

    public CashierRemoveCommandHandler(CashierService cashierManager, TicketService ticketManager) {
        this.cashierManager = cashierManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {

        String id = tokens.next();

        if (id == null) return;

        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_CASHIER_REMOVE);
            return;
        }

        if (!cashierManager.existId(id)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        boolean removed = cashierManager.removeCashierAndTickets(id, ticketManager);

        if (removed) System.out.println(CASHIER_REMOVE_OK);
        else System.out.println(CASHIER_REMOVE_FAIL);
    }
}
