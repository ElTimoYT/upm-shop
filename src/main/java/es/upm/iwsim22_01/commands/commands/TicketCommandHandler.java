package es.upm.iwsim22_01.commands.commands;

import java.util.*;

import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.PersonalizableProduct;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

public class TicketCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET = "Incorrect use: ticket new|add|remove|print",
            ERROR_INCORRECT_USE_TICKET_PRINT = "Incorrect use: ticket print <ticketId> <cashId>",
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<ticketId>] <cashId> <userId>",
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]",
            ERROR_INCORRECT_USE_TICKET_REMOVE = "Incorrect use: ticket remove <ticketId> <cashId> <prodId>",
            ERROR_PRODUCT_IS_NO_PERSONALIZABLE = "Product is not personalizable",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INVALID_ID = "Invalid id",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_NEW_OK = "ticket new: ok",
            TICKET_PRINT_OK = "ticket print: ok",
            TICKET_ADD_OK = "ticket add: ok",
            TICKET_REMOVAL_OK = "ticket remove: ok",
            TICKET_LIST_OK = "ticket list: ok",
            TICKET_LIST = "Tickets:",

            NEW = "new",
            ADD = "add",
            REMOVE = "remove",
            PRINT = "print",
            LIST = "list";

    private TicketManager ticketManager;
    private ProductManager productManager;
    private CashierManager cashierManager;
    private ClientManager clientManager;

    public TicketCommandHandler(TicketManager ticketManager, ProductManager productManager, CashierManager cashierManager, ClientManager clientManager) {
        this.ticketManager = ticketManager;
        this.productManager = productManager;
        this.cashierManager = cashierManager;
        this.clientManager = clientManager;
    }

    @Override
    public void runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET);
            return;
        }

        switch (tokens.next()) {
            case NEW -> newTickedCommand(tokens);
            case ADD -> addTicketCommand(tokens);
            case REMOVE -> removeTicketCommand(tokens);
            case PRINT -> printTicketCommand(tokens);
            case LIST -> listTicketCommand(tokens);
            default -> System.out.println(ERROR_INCORRECT_USE_TICKET);
        };
    }

    private void printTicketCommand(Iterator<String> tokens) {
        //ticketId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
            return;
        }
        OptionalInt ticketId = Converter.stringToInt(tokens.next());
        if (ticketId.isEmpty() || !ticketManager.existId(ticketId.getAsInt())) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return;
        }

        //cashierId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_PRINT);
            return;
        }
        String cashierId = tokens.next();
        if (!clientManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        if (ticketManager.existId(ticketId.getAsInt())) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return;
        }
        Ticket ticket = ticketManager.get(ticketId.getAsInt());

        ticket.closeTicket();
        System.out.println(ticket);
        System.out.println(TICKET_PRINT_OK);
    }

    private void removeTicketCommand(Iterator<String> tokens) {
        //ticketId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return;
        }
        OptionalInt ticketId = Converter.stringToInt(tokens.next());
        if (ticketId.isEmpty() || !ticketManager.existId(ticketId.getAsInt())) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return;
        }
        Ticket ticket = ticketManager.get(ticketId.getAsInt());

        //cashierId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return;
        }
        String cashierId = tokens.next();
        if (!cashierManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        //productId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty() || !productManager.existId(productId.getAsInt())) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }
        Product product = productManager.get(productId.getAsInt());

        ticket.removeProduct(product);
        System.out.println(ticket);
        System.out.println(product);
        System.out.println(TICKET_REMOVAL_OK);
    }

    private void addTicketCommand(Iterator<String> tokens) {
        //ticketId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return;
        }
        OptionalInt ticketId = Converter.stringToInt(tokens.next());
        if (ticketId.isEmpty() || !ticketManager.existId(ticketId.getAsInt())) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return;
        }
        Ticket ticket = ticketManager.get(ticketId.getAsInt());

        //cashierId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return;
        }
        String cashierId = tokens.next();
        if (!cashierManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }

        //productId
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty() || !productManager.existId(productId.getAsInt())) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
        }
        Product product = productManager.get(productId.getAsInt());

        //amount
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return;
        }
        OptionalInt amount = Converter.stringToInt(tokens.next());
        if (amount.isEmpty() || amount.getAsInt() < 0 || amount.getAsInt() > Ticket.MAX_PRODUCTS) {
            System.out.println(ERROR_INVALID_AMOUNT);
            return;
        }

        //personalizableTexts
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            if (product instanceof PersonalizableProduct personalizableProduct) {
                ticket.addProduct(personalizableProduct, amount.getAsInt(), tokens.next().split("--p"));
            } else {
                System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                return;
            }
        } else {
            ticket.addProduct(product, amount.getAsInt());
        }

        System.out.println(ticket);
        System.out.println(product);
        System.out.println(TICKET_ADD_OK);
    }

    private void newTickedCommand(Iterator<String> tokens) {
        //1º token
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
        String token1 = tokens.next();

        //2º token
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_NEW);
        }
        String token2 = tokens.next();

        if (!tokens.hasNext()) {
            newTicketCommandWithoutId(token1, token2);
        } else {
            newTicketCommandWithId(token1, token2, tokens.next());
        }
    }

    private void newTicketCommandWithoutId(String cashId, String clientId) {
       if (!cashierManager.existId(cashId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return;
       }

       if (!clientManager.existId(clientId)) {
           System.out.println(ERROR_CLIENT_NOT_FOUND);
           return;
       }

        Ticket ticket = ticketManager.addTicket();
        cashierManager.get(cashId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);

        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(String stringId, String cashId, String clientId) {
        OptionalInt id = Converter.stringToInt(stringId);
        if (id.isEmpty() || ticketManager.existId(id.getAsInt())) {{
            System.out.println(ERROR_INVALID_ID);
            return;
        }}

        Ticket ticket = ticketManager.addTicket(id.getAsInt());
        cashierManager.get(cashId).addTicket(ticket);
        clientManager.get(clientId).addTicket(ticket);


        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }

    private void listTicketCommand(Iterator<String> tokens) {
        List<Ticket> allTickets = ticketManager.getAll();
        allTickets.sort(Comparator.comparing(Ticket::getInitialDate));

        System.out.println(TICKET_LIST);
        allTickets.forEach(System.out::println);
        System.out.println(TICKET_LIST_OK);
    }
}
