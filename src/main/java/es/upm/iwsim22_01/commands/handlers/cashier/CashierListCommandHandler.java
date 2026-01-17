package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.CashierInventory;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;

import java.util.Collection;
import java.util.Comparator;

public class CashierListCommandHandler implements CommandHandler {

private static final String CASHIER_LIST_OK = "cash list: ok";

    private final CashierInventory cashierService;

    public CashierListCommandHandler(CashierInventory cashierService) {
        this.cashierService = cashierService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        Collection<CashierDTO> cashiers = cashierService.getAll();

        System.out.println("Cash:");
        if (!cashiers.isEmpty()) {
            cashiers.stream()
                    .sorted(Comparator.comparing(CashierDTO::getName))
                    .forEach(c -> System.out.println("  " + c));
        }
        System.out.println(CASHIER_LIST_OK);
    }
}
