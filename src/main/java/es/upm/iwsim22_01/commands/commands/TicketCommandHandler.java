package es.upm.iwsim22_01.commands.commands;

import java.util.*;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Product;

public class TicketCommandHandler implements CommandHandler {
    private static final String
            ERROR_NO_TICKET = "No ticket created",
            ERROR_INCORRECT_USE_TICKET = "Incorrect use: ticket new|add|remove|print",
            ERROR_INCORRECT_USE_TICKET_NEW = "Incorrect use: ticket new [<id>] <cashId> <userId>",
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

    public TicketCommandHandler(TicketManager ticketManager, ProductManager productManager) {
        this.ticketManager = ticketManager;
        this.productManager = productManager;
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
            default -> System.out.println(ERROR_INCORRECT_USE_TICKET);
        };
    }

    private void printTicketCommand(Iterator<String> tokens) {
        /*if (!App.existsTicket()) {
            System.out.println(ERROR_NO_TICKET);
            return;
        }
        System.out.println(App.getCurrentTicket());

        App.resetTicket();

        System.out.println(TICKET_PRINT_OK);*/
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

    private void newTicketCommandWithoutId(String cashId, String productId) {

    }

    private void newTicketCommandWithId(String id, String cashId, String productId) {

    }
}
