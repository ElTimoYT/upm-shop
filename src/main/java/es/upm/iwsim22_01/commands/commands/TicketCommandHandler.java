package es.upm.iwsim22_01.commands.commands;

import java.util.*;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

public class TicketCommandHandler implements CommandHandler {
    private static final String
            ERROR_NO_TICKET = "No ticket created",
            ERROR_INCORRECT_USE_TICKET = "Incorrect use: ticket new|add|remove|print",
            ERROR_INCORRECT_USE_TICKET_PRINT = "Incorrect use: ticket print <ticketId> <cashId>",
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<ticketId>] <cashId> <userId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_CLIENT_NOT_FOUND = "Client not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <prodId> <amount>",
            ERROR_INCORRECT_USE_TICKET_REMOVAL = "Incorrect use: ticket remove <prodId>",
            ERROR_INVALID_ID = "Invalid id",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",
            ERROR_UNABLE_TO_ADD_PRODUCT = "Unable to add the products",
            ERROR_UNABLE_TO_REMOVE_PRODUCT = "Unable to remove the product",

            TICKET_NEW_OK = "ticket new: ok",
            TICKET_PRINT_OK = "ticket print: ok",
            TICKET_ADD_OK = "ticket add: ok",
            TICKET_REMOVAL_OK = "ticket remove: ok",

            NEW = "new",
            ADD = "add",
            REMOVE = "remove",
            PRINT = "print";

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
            /*case ADD -> addTicketCommand(tokens);
            case REMOVE -> removeTicketCommand(tokens);*/
            case PRINT -> printTicketCommand(tokens);
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

        Optional<Ticket> ticket = ticketManager.get(ticketId.getAsInt());
        if (ticket.isEmpty()) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return;
        }

        ticket.get().closeTicket();
        System.out.println(ticket.get());
        System.out.println(TICKET_PRINT_OK);
    }

    private void removeTicketCommand(Iterator<String> tokens) {
        /*//Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVAL);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }


        if (!App.existsTicket()) {
            System.out.println(ERROR_NO_TICKET);
            return;
        }

        if (App.getCurrentTicket().removeProductById(productId.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            System.out.println(TICKET_REMOVAL_OK);
        } else {
            System.out.println(ERROR_UNABLE_TO_REMOVE_PRODUCT);
        }*/
    }

    private void addTicketCommand(Iterator<String> tokens) {
    /*
        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_UNABLE_TO_REMOVE_PRODUCT);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if (productManager.existId(productId.getAsInt())) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }

        //Amount
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return;
        }
        OptionalInt amount = Converter.stringToInt(tokens.next());
        if (amount.isEmpty() || amount.getAsInt() <= 0) {
            System.out.println(ERROR_INVALID_AMOUNT);
            return;
        }

        if (!App.existsTicket()) {
            System.out.println(ERROR_NO_TICKET);
            return;
        }

        if (App.getCurrentTicket().addProduct(productId.getAsInt(), amount.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            System.out.println(TICKET_ADD_OK);
        } else {
            System.out.println(ERROR_UNABLE_TO_ADD_PRODUCT);
        }*/
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

        Ticket ticket = ticketManager.addTicket(cashId, clientId);
        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }

    private void newTicketCommandWithId(String stringId, String cashId, String productId) {
        OptionalInt id = Converter.stringToInt(stringId);
        if (id.isEmpty() || ticketManager.existId(id.getAsInt())) {{
            System.out.println(ERROR_INVALID_ID);
            return;
        }}

        Ticket ticket = ticketManager.addTicket(id.getAsInt(), cashId, productId);
        System.out.println(ticket);
        System.out.println(TICKET_NEW_OK);
    }
}
