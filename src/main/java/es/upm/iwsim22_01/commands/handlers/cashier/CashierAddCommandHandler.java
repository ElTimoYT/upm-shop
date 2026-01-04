package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;

public class CashierAddCommandHandler implements CommandHandler {

    private static final String
            ERROR_INCORRECT_USE_CASHIER_ADD = "Incorrect use: cash add [<id>] \"<name>\" <email>",
            ERROR_INVALID_ID = "Cashier id already exits",
            ERROR_INVALID_ID_FORMAT = "Cashier id format is invalid",
            CASHIER_ADD_OK = "cash add: ok",
            CASHIER_ADD_FAIL = "cash add: fail";

    private CashierService cashierManager;

    public CashierAddCommandHandler(CashierService cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        String id = null;
        String name;
        String email;
        CashierDTO cashier;

        int remaining = tokens.getRemainingTokens();
        try {
            if (remaining == 3) {
                id = tokens.next();
                if (cashierManager.existsId(id)) {
                    System.out.println(ERROR_INVALID_ID);
                    return;
                }

                if (!cashierManager.checkId(id)){
                    System.out.println(ERROR_INVALID_ID_FORMAT);
                    return;
                }

                name = tokens.next();
                email = tokens.next();

                cashier = cashierManager.addCashier(name, email, id);
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
