package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.TicketManager;

public class CashierRemoveCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER_REMOVE = "Incorrect use: cashier remove <id>",
            CASHIER_REMOVE_OK = "cash remove: ok",
            CASHIER_REMOVE_FAIL = "cash remove: fail";

    private CashierManager cashierManager;
    private TicketManager ticketManager;

    public CashierRemoveCommandHandler(CashierManager cashierManager, TicketManager ticketManager) {
        this.cashierManager = cashierManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        String id = tokens.nextAsStringId(cashierManager, true, ERROR_INCORRECT_USE_CASHIER_REMOVE, ERROR_INCORRECT_USE_CASHIER_REMOVE);

        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_CASHIER_REMOVE);
            return;
        }

        boolean removed = cashierManager.removeCashierAndTickets(id, ticketManager);

        if (removed) System.out.println(CASHIER_REMOVE_OK);
        else System.out.println(CASHIER_REMOVE_FAIL);
    }
}
