package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Ticket;

import java.util.*;
import java.util.stream.Collectors;

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

        try {
            if (args.size() == 2) {
                name = args.get(0);
                email = args.get(1);

                Cashier cashier = cashierManager.addCashier(name, email);
            } else if (args.size() == 3) {
                id = args.get(0);
                name = args.get(1);
                email = args.get(2);
                Cashier cashier = cashierManager.addCashier(id, name, email);
            } else {
                System.out.println("Incorrect use: cashier add [<id>] \"<name>\" <email>");
                return;
            }
            System.out.println("cashier add: ok");

        }catch (Exception e) {
            System.out.println("Error adding cashier");
        }
    }

    private void listCashierCommand(){
        Collection<Cashier> cashiers = cashierManager.getAll();

        if (cashiers.isEmpty()) System.out.println("No cashiers found.");

        List<Cashier> cashierList = cashiers.stream()
                .sorted(Comparator.comparing(Cashier::getName))
                .toList();

        System.out.println("Cashiers:");
        for (Cashier c : cashierList) {
            System.out.printf("ID: %s | Name: %s | Email: %s%n",
                    c.getId(), c.getName(), c.getEmail());
        }

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
                        Collection<Ticket> tickets = cashier.getTickets();
                        if (tickets == null || tickets.isEmpty()){
                            System.out.println("No tickets found.");
                            return;
                        }

                        List<Ticket> sortedTickets = tickets.stream()
                                .sorted(Comparator.comparing(Ticket::getId))
                                .toList();
                        System.out.println("Tickets:");
                        for (Ticket t : sortedTickets) {
                            System.out.printf("ID: %s | Estado: %s%n",
                                    t.getId(), t.getState());
                        }
                    },
                    () -> System.out.println("Cashier not found.")
            );
        }catch (Exception e) {
            System.out.println("Incorrect use: cashier tickets <id>");
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
                return;
            }

            Cashier cashier = optionalCashier.get();
            cashierManager.remove(cashier.getId());
            System.out.println("Cashier removed: " + cashier.getId());
        }catch (Exception e) {
            System.out.println("Incorrect use: cashier remove <id>");
        }
    }

}
