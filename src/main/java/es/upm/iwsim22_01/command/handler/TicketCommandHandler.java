package es.upm.iwsim22_01.command.handler;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Product;

import java.util.*;
import java.util.function.Function;

public class TicketCommandHandler {
    private static final Map<String, Function<Iterator<String>, CommandStatus>> TICKET_SUBCOMMANDS = Map.of(
        "new", TicketCommandHandler::newTickedCommand,
        "add", TicketCommandHandler::addTicketCommand,
        "remove", TicketCommandHandler::removeTicketCommand,
        "print", TicketCommandHandler::printTicketCommand
    );

    public static CommandStatus runCommand(Iterator<String> tokens) {
        CommandStatus incorrectUsage = new CommandStatus(false, "Incorrect use: ticket new|add|remove|print");

        if (!tokens.hasNext()) {
            return incorrectUsage;
        }

        return TICKET_SUBCOMMANDS.getOrDefault(
                tokens.next(),
                stringIterator -> incorrectUsage
        ).apply(tokens);
    }

    private static CommandStatus printTicketCommand(Iterator<String> tokens) {
        if (!App.existsTicket()) {
            return new CommandStatus(false, "No ticket created");
        }
        System.out.println(App.getCurrentTicket());

        return new CommandStatus(true, "ticket print: ok");
    }

    private static CommandStatus removeTicketCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: ticket remove <prodId>");
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            return new CommandStatus(false, "Invalid id");
        }


        if (!App.existsTicket()) {
            return new CommandStatus(false, "No ticket created");
        }
        App.getCurrentTicket().removeProductById(productId.getAsInt());
        return new CommandStatus(true, "ticket remove: ok");
    }

    private static CommandStatus addTicketCommand(Iterator<String> tokens) {
        CommandStatus incorrectUse = new CommandStatus(false, "Incorrect use: ticket add <prodId> <amount>");

        //Id
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            return new CommandStatus(false, "Invalid id");
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            return new CommandStatus(false, "Product not found");
        }

        //Amount
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalInt amount = Converter.stringToInt(tokens.next());
        if (amount.isEmpty() || amount.getAsInt() <= 0) {
            return new CommandStatus(false, "Invalid amount");
        }

        if (!App.existsTicket()) {
            return new CommandStatus(false, "No ticket created");
        }
        App.getCurrentTicket().addProduct(optionalProduct.get(), amount.getAsInt());
        return new CommandStatus(true, "ticket add: ok");
    }

    private static CommandStatus newTickedCommand(Iterator<String> tokens) {
        App.resetTicket();
        return new CommandStatus(true, "ticket new: ok");
    }
}
