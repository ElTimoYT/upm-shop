package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.CommandStatus;
import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Product;

/**
 * Manejador del comando 'ticket' para gestionar tickets de compra.
 * <p>
 * Este comando permite crear nuevos tickets, añadir productos,
 * eliminar productos e imprimir el ticket con los descuentos aplicados.
 * </p>
 */
public class TicketCommandHandler implements CommandHandler {
    
    /**
     * Ejecuta el comando ticket delegando a la suboperación correspondiente.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        CommandStatus incorrectUsage = new CommandStatus(false, "Incorrect use: ticket new|add|remove|print");

        if (!tokens.hasNext()) {
            return incorrectUsage;
        }

        return switch (tokens.next()) {
            case "new" -> newTickedCommand(tokens);
            case "add" -> addTicketCommand(tokens);
            case "remove" -> removeTicketCommand(tokens);
            case "print" -> printTicketCommand(tokens);
            default -> incorrectUsage;
        };
    }

    /**
     * Maneja la suboperación 'print' del comando ticket.
     * Imprime el ticket actual y lo resetea.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus printTicketCommand(Iterator<String> tokens) {
        if (!App.existsTicket()) {
            return new CommandStatus(false, "No ticket created");
        }
        System.out.println(App.getCurrentTicket());

        App.resetTicket();

        return new CommandStatus(true, "ticket print: ok");
    }

    /**
     * Maneja la suboperación 'remove' del comando ticket.
     * Elimina productos del ticket actual.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus removeTicketCommand(Iterator<String> tokens) {
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

        if (App.getCurrentTicket().removeProductById(productId.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            return new CommandStatus(true, "ticket remove: ok");
        } else {
            return new CommandStatus(false, "Unable to remove the product");
        }
    }

    /**
     * Maneja la suboperación 'add' del comando ticket.
     * Añade productos al ticket actual.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus addTicketCommand(Iterator<String> tokens) {
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

        if (App.getCurrentTicket().addProduct(optionalProduct.get(), amount.getAsInt())) {
            System.out.println(App.getCurrentTicket());
            return new CommandStatus(true, "ticket add: ok");
        } else {
            return new CommandStatus(false, "Unable to add the products");
        }
    }

    /**
     * Maneja la suboperación 'new' del comando ticket.
     * Crea un nuevo ticket vacío.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus newTickedCommand(Iterator<String> tokens) {
        App.resetTicket();
        return new CommandStatus(true, "ticket new: ok");
    }
}
