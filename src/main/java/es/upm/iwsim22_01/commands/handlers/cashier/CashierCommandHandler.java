package es.upm.iwsim22_01.commands.handlers.cashier;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Ticket;

import java.util.*;

public class CashierCommandHandler implements CommandHandler {

    private CashierManager cashierManager;
    private TicketManager ticketManager;

    public CashierCommandHandler(CashierManager cashierManager, TicketManager ticketManager) {
        this.cashierManager = cashierManager;
        this.ticketManager = ticketManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
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

    private void addCashierCommand(CommandTokens tokens) {

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

    private void showTicketsCashierCommand(CommandTokens tokens) {
        try {
            String id = tokens.next();
            if (tokens.hasNext()) {
                System.out.println("Incorrect use: cashier tickets <id>");
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
                        System.out.println("cash tickets: ok");
                    },
                    () -> System.out.println("cash tickets: fail")
            );
        }catch (Exception e) {
            System.out.println("cash tickets: fail");
        }
    }

    private void removeCashierCommand(CommandTokens tokens) {
            String id = tokens.next();

            if (tokens.hasNext()) {
                System.out.println("Incorrect use: cashier remove <id>");
                return;
            }

            boolean removed = cashierManager.removeCashierAndTickets(id, ticketManager);

            if (removed) System.out.println("cash remove: ok");
            else System.out.println("cash remove: fail");
    }
}
