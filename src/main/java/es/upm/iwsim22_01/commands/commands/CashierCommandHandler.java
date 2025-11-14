package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Ticket;

import java.util.*;

public class CashierCommandHandler implements CommandHandler {

    private CashierManager cashierManager;

    public CashierCommandHandler(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            System.out.println("No command provided.");
            return;
        }

        String command = tokens.next();

        switch (command) {
            case "add" -> addCashierCommand(tokens);
            case "list" -> listCashierCommand();
            case "tickets" -> showTicketsCashierCommand(tokens);
            case "remove" -> removeCashierCommand(tokens);
            default -> System.out.println("Incorrect use: cashier add|list|tickets|remove");
        };
    }

    private void addCashierCommand(Iterator<String> tokens) {

        List<String> args = new ArrayList<>();
        tokens.forEachRemaining(args::add);

        String id = null;
        String name;
        String email;

        Cashier cashier;

        try {
            if (args.size() == 2) {
                name = args.get(0);
                email = args.get(1);

                cashier = cashierManager.addCashier(name, email);
            } else if (args.size() == 3) {
                id = args.get(0);
                name = args.get(1);
                email = args.get(2);
                cashier = cashierManager.addCashier(id, name, email);
            } else {
                System.out.println("Incorrect use: cashier add [<id>] \"<name>\" <email>");
                return;
            }
            System.out.println(cashier);
            System.out.println("cash add: ok");

        }catch (Exception e) {
            System.out.println("cash add: fail");
        }
    }

    private void listCashierCommand(){
        Collection<Cashier> cashiers = cashierManager.getAll();

        if (cashiers.isEmpty()) System.out.println("No cashiers found.");

        System.out.println("Cash:");
        if (!cashiers.isEmpty()) {
            cashiers.stream()
                    .sorted(Comparator.comparing(Cashier::getName))
                    .forEach(c -> System.out.println("  " + c));
        }
        System.out.println("cash list: ok");

    }

    private void showTicketsCashierCommand(Iterator<String> tokens) {
        try {
            String id = tokens.next();
            if (tokens.hasNext()) {
                System.out.println("Incorrect use: cashier tickets <id>");
                return;
            }
            Optional<Cashier> optionalCashier = cashierManager.get(id);
            optionalCashier.ifPresentOrElse(
                    cashier -> {
                        System.out.println("Tickets: ");
                        Collection<Ticket> tickets = cashier.getTickets();

                        if (tickets != null && !tickets.isEmpty()) {
                            tickets.stream()
                                    .sorted(Comparator.comparing(Ticket::getId))
                                    .forEach(t -> System.out.println("  " + t.getId() + "->" + t.getState()));
                        }
                        System.out.println("cash tickets: ok");
                    },
                    () -> System.out.println("cash tickets: fail")
            );
        }catch (Exception e) {
            System.out.println("cash tickets: fail");
        }
    }

    private void removeCashierCommand(Iterator<String> tokens) {
        try {
            String id = tokens.next();
            if (tokens.hasNext()) {
                System.out.println("Incorrect use: cashier remove <id>");
                return;
            }
            Optional<Cashier> optionalCashier = cashierManager.get(id);
            if (optionalCashier.isEmpty()) {
                System.out.println("Cashier not found.");
                System.out.println("cash remove: fail");
                return;
            }

            Cashier cashier = optionalCashier.get();
            cashierManager.remove(cashier.getId());
            System.out.println("cash remove: ok");
        }catch (Exception e) {
            System.out.println("cash remove: fail");
        }
    }

}
