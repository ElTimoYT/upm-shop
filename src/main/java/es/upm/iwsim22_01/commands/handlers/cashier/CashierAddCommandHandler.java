package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.Cashier;

public class CashierAddCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER_ADD = "Incorrect use: cashier add [<id>] \"<name>\" <email>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            CASHIER_ADD_OK = "cash add: ok",
            CASHIER_ADD_FAIL = "cash add: fail";

    private CashierManager cashierManager;

    public CashierAddCommandHandler(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        String id = null;
        String name;
        String email;
        Cashier cashier;

        int remaining = tokens.getRemainingTokens();
        try {
            if (remaining == 3) {
                id = tokens.nextAsStringId(cashierManager, true, ERROR_INCORRECT_USE_CASHIER_ADD, ERROR_CASHIER_NOT_FOUND);
                name = tokens.next();
                email = tokens.next();

                cashier = new Cashier(id, name, email);
            } else if (remaining == 2) {
                name = tokens.next();
                email = tokens.next();
                cashier = cashierManager.addCashier(name, email);
            } else {
                System.out.println(ERROR_INCORRECT_USE_CASHIER_ADD);
                return;
            }
            System.out.println(cashier);
            System.out.println(CASHIER_ADD_OK);

        }catch (Exception e) {
            System.out.println(CASHIER_ADD_FAIL);
        }
    }
}
