package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;

import java.util.Collection;
import java.util.Comparator;

public class CashierListCommandHandler implements CommandHandler {

private static final String ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            CASHIER_LIST_OK = "cash list: ok";

    private CashierService cashierManager;

    public CashierListCommandHandler(CashierService cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        Collection<CashierDTO> cashiers = cashierManager.getAll();

        if (cashiers.isEmpty()) System.out.println(ERROR_CASHIER_NOT_FOUND);

        System.out.println("Cash:");
        if (!cashiers.isEmpty()) {
            cashiers.stream()
                    .sorted(Comparator.comparing(CashierDTO::getName))
                    .forEach(c -> System.out.println("  " + c));
        }
        System.out.println(CASHIER_LIST_OK);
    }
}
