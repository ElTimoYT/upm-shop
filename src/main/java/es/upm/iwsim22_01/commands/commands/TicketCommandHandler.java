package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Product;

public class TicketCommandHandler implements CommandHandler {

    @Override
    public void runCommand(Iterator<String> tokens) {
        String incorrectUsage = "Incorrect use: ticket new|add|remove|print";

        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }

        switch (tokens.next()) {
            case "new" -> newTickedCommand(tokens);
            case "add" -> addTicketCommand(tokens);
            case "remove" -> removeTicketCommand(tokens);
            case "print" -> printTicketCommand(tokens);
            default -> System.out.println(incorrectUsage);
        };
    }

    private void printTicketCommand(Iterator<String> tokens) {
        if (!App.existsTicket()) {
            System.out.println("No ticket created");
            return;
        }
        System.out.println(App.getCurrentTicket());

        App.resetTicket();

        System.out.println("ticket print: ok");
    }

    private void removeTicketCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println("Incorrect use: ticket remove <prodId>");
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println("Invalid id");
            return;
        }


        if (!App.existsTicket()) {
            System.out.println("No ticket created");
            return;
        }

        if (App.getCurrentTicket().removeProductById(productId.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            System.out.println("ticket remove: ok");
        } else {
            System.out.println("Unable to remove the product");
        }
    }

    private void addTicketCommand(Iterator<String> tokens) {
        String incorrectUse = "Incorrect use: ticket add <prodId> <amount>";

        //Id
        if (!tokens.hasNext()) {
            System.out.println("Unable to remove the product");
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println("Invalid id");
            return;
        }

        if (ProductManager.getProductManager().existId(productId.getAsInt())) {
            System.out.println("Product not found");
            return;
        }

        //Amount
        if (!tokens.hasNext()) {
            System.out.println(incorrectUse);
            return;
        }
        OptionalInt amount = Converter.stringToInt(tokens.next());
        if (amount.isEmpty() || amount.getAsInt() <= 0) {
            System.out.println("Invalid amount");
            return;
        }

        if (!App.existsTicket()) {
            System.out.println("No ticket created");
            return;
        }

        if (App.getCurrentTicket().addProduct(productId.getAsInt(), amount.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            System.out.println("ticket add: ok");
        } else {
            System.out.println("Unable to add the products");
        }
    }

    private void newTickedCommand(Iterator<String> tokens) {
        App.resetTicket();
        System.out.println("ticket new: ok");
    }
}
