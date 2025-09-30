package es.upm.iwsim22_01;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.Converter;
import es.upm.iwsim22_01.manager.ProductManager;

import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static boolean menu = true;
    private static Ticket ticket = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        Iterator<String> tokens;

        while (menu) {
            tokens = tokenizeCommand(scanner.nextLine());

            CommandStatus commandStatus = executeCommand(tokens);
            if (!commandStatus.getStatus()) {
                System.err.println(commandStatus.getMessage());
            } else if (commandStatus.getMessage() != null) {
                System.out.println(commandStatus.getMessage());
            }
        }

        System.out.println("""
                Closing application.
                Goodbye!
                """);
    }

    private static Iterator<String> tokenizeCommand(String command) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"|\\S+"); //Dividimos por espacios o comillas
        Matcher matcher = pattern.matcher(command);

        List<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            String token = matcher.group();
            if (token.startsWith("\"") && token.endsWith("\"")) { //Si está entre comillas quitar las comillas.
                tokens.add(token.substring(1, token.length() -1));
            } else { //Poner en minusculas si no
                tokens.add(token.toLowerCase());
            }
        }

        return tokens.iterator();
    }

    private static CommandStatus executeCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "No command detected");
        }

        switch (tokens.next()) {
            case "exit" -> {
                menu = false;

                return new CommandStatus(true);
            }
            case "echo" -> {
                if (!tokens.hasNext()) {
                    return new CommandStatus(false, "Incorrect use: echo \"<message>\"");
                }

                System.out.println(tokens.next());
                return new CommandStatus(true);
            }
            case "help" -> {
                helpMenu();

                return new CommandStatus(true);
            }
            case "prod" -> {
                return productCommand(tokens);
            }
            case "ticket" -> {
                return ticketCommand(tokens);
            }
            default -> {
                return new CommandStatus(false, "Unknown command");
            }
        }
    }

    private static CommandStatus productCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: prod add|list|update|remove");
        }

        switch (tokens.next()) {
            case "add" -> {
                return addProductCommand(tokens);
            }
            case "list" -> {
                return listProductCommand(tokens);
            }
            case "update" -> {
                return updateProductCommand(tokens);
            }
            case "remove" -> {
                return removeProductCommand(tokens);
            }
            default -> {
                return new CommandStatus(false, "Incorrect use: prod add|list|update|remove");
            }
        }
    }

    private static CommandStatus addProductCommand(Iterator<String> tokens) {
        CommandStatus incorrectUse = new CommandStatus(false, "Incorrect use: prod add <id> \"<name>\" <category> <price>");

        //Id
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (!Product.checkId(productId)) {
            return new CommandStatus(false, "Invalid id");
        }

        //Name
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        String productName = tokens.next();
        if (!Product.checkName(productName)) {
            return new CommandStatus(false, "Invalid name");
        }

        //Category
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
        if (productCategory.isEmpty()) {
            return new CommandStatus(false, "Invalid category");
        }

        //Price
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
        if (!Product.checkPrice(productPrice)) {
            return new CommandStatus(false, "Invalid price");
        }

        Product product = new Product(
                productId.getAsInt(),
                productName,
                productCategory.get(),
                productPrice.getAsDouble()
        );

        if (ProductManager.getProductManager().addProduct(product)) {
            return new CommandStatus(true, "prod add: ok");
        } else {
            return new CommandStatus(false, "Unknown error");
        }
    }

    private static CommandStatus listProductCommand(Iterator<String> tokens) {
        System.out.println("Catalog:");
        ProductManager.getProductManager().getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        return new CommandStatus(true, "prod list: ok");
    }

    private static CommandStatus updateProductCommand(Iterator<String> tokens) {
        CommandStatus incorrectUse = new CommandStatus(false, "Incorrect use: prod update <id> name|category|price <value>");

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

        //Parametro a cambiar
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        switch (tokens.next()) {
            case "name" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                String productName = tokens.next();
                if (!Product.checkName(productName)) {
                    return new CommandStatus(false, "Invalid name");
                }

                optionalProduct.get().setName(productName);
                return new CommandStatus(true, "prod update: ok");
            }
            case "category" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
                if (productCategory.isEmpty()) {
                    return new CommandStatus(false, "Invalid category");
                }

                optionalProduct.get().setCategory(productCategory.get());
                return new CommandStatus(true, "prod update: ok");
            }
            case "price" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
                if (!Product.checkPrice(productPrice)) {
                    return new CommandStatus(false, "Invalid price");
                }

                optionalProduct.get().setPrice(productPrice.getAsDouble());
                return new CommandStatus(true, "prod update: ok");
            }
            default -> {
                return incorrectUse;
            }
        }
    }

    private static CommandStatus removeProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: prod remove <id>");
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            return new CommandStatus(false, "Invalid id");
        }

        ProductManager.getProductManager().removeProduct(productId.getAsInt());
        return new CommandStatus(true, "prod remove: ok");
    }

    private static CommandStatus ticketCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: ticket new|add|remove|print");
        }

        switch (tokens.next()) {
            case "new" -> {
                return newTickedCommand(tokens);
            }
            case "add" -> {
                return addTicketCommand(tokens);
            }
            case "remove" -> {
                return removeTicketCommand(tokens);
            }
            case "print" -> {
                return printTicketCommand(tokens);
            }
            default -> {
                return new CommandStatus(false, "Incorrect use: prod add|list|update|remove");
            }
        }
    }

    private static CommandStatus printTicketCommand(Iterator<String> tokens) {
        if (ticket == null) {
            return new CommandStatus(false, "No ticket created");
        }
        System.out.println(ticket);

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


        if (ticket == null) {
            return new CommandStatus(false, "No ticket created");
        }
        ticket.removeProductById(productId.getAsInt());
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

        if (ticket == null) {
            return new CommandStatus(false, "No ticket created");
        }
        ticket.addProduct(optionalProduct.get(), amount.getAsInt());
        return new CommandStatus(true, "ticket add: ok");
    }

    private static CommandStatus newTickedCommand(Iterator<String> tokens) {
        ticket = new Ticket();
        return new CommandStatus(true, "ticket new: ok");
    }

    public static void helpMenu() {
        System.out.println("""
                Commands:
                 prod add <id> "<name>" <category> <price>
                 prod list
                 prod update <id> NAME|CATEGORY|PRICE <value>
                 prod remove <id>
                 ticket new
                 ticket add <prodId> <quantity>
                 ticket remove <prodId>
                 ticket print
                 echo "<texto>"
                 help
                 exit
                 
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,
                ELECTRONICS 3%.
                """);
    }

}

