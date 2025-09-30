package es.upm.iwsim22_01;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.manager.ProductManager;

import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private final static ProductManager PRODUCT_MANAGER = new ProductManager();
    private static boolean menu = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager manager = new ProductManager();
        Ticket ticket = new Ticket();

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        Iterator<String> tokens;

        int id = 0;
        int quantity = 0;
        while (menu) {
            tokens = tokenizeCommand(scanner.nextLine());

            CommandStatus commandStatus = executeCommand(tokens);
            if (!commandStatus.getStatus()) {
                System.err.println(commandStatus.getMessage());
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
            tokens.add(matcher.group().replaceAll("^\"|\"$", "")); //Quitar las comillas del principio y final
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
        return null;
    }

    private static CommandStatus ticketCommand(Iterator<String> tokens) {
        return null;
    }

    /*private static CommandStatus removeProductCommand(String[] command) {
        int id = stringToInt(command[2]);
        if (id == Integer.MIN_VALUE) {
            return new CommandStatus(false, "No se ha podido convertir el numero");
        }

        App.PRODUCT_MANAGER.removeProduct(id)
        return new CommandStatus(true, "");
    }

    private static boolean updateProductCommand(String[] command) {
        int id = stringToInt(command[2]);
        if (id == Integer.MIN_VALUE) {
            return false;
        }

        Optional<Product> optionalProduct = App.PRODUCT_MANAGER.getProduct(id);
        if (optionalProduct.isEmpty()) {
            return false;
        }
        switch (command[3]) {
            case "NAME":
                int index = 4;
                StringBuilder productName = new StringBuilder();
                do {
                    productName.append(command[index++]).append(" ");
                } while (productName.charAt(productName.length() -2) != '\"');
                productName.delete(productName.length() -1, productName.length());

                return optionalProduct.get().setName(productName.toString());
            case "CATEGORY":
                Category category = stringToCategory(command[4]);
                if (category == null) {
                    return false;
                }

                optionalProduct.get().setCategory(category);
                return true;
            case "PRICE":
                double price = stringToDouble(command[4]);
                if (price == Double.MIN_VALUE) {
                    return false;
                }

                optionalProduct.get().setPrice(price);
                return true;
            default:
                return false;
        }
    }

    private static boolean listProductsCommand(String[] command) {
        System.out.println("Catalog:");
        App.PRODUCT_MANAGER.getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        return true;
    }

    private static boolean addProductCommand(String[] command) {
        int id = stringToInt(command[2]);
        if (id == Integer.MIN_VALUE) {
            return false;
        }

        int index = 3;
        StringBuilder productName = new StringBuilder();
        do {
            productName.append(command[index++]).append(" ");
        } while (productName.charAt(productName.length() -2) != '\"');
        productName.delete(productName.length() -1, productName.length());

        Category productCategory = stringToCategory(command[index++]);
        if (productCategory == null) {
            return false;
        }

        double productPrice = stringToDouble(command[index]);
        if (productPrice == Double.MIN_VALUE) {
            return false;
        }

        Product product = Product.createNewProduct(id, productName.toString().replace("\"", ""), productCategory, productPrice);
        if (product == null) {
            return false;
        }

        return App.PRODUCT_MANAGER.addProduct(product);
    }*/

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

    public static boolean addTicket(String[] command, ProductManager productManager, Ticket ticket) {

        int id = Integer.parseInt(command[2]);
        int quantity = Integer.parseInt(command[3]);

        boolean exists = idExists(command, productManager);
        Optional<Product> product = productManager.getProduct(id);

        if (exists) {
            return ticket.addProduct(product.get(), quantity);
        } else {
            System.out.println("ticket add: error - product id " + id + " does not exist.");
            return false;
        }
    }

    public static boolean idExists(String[] command, ProductManager productManager) {
        int id = Integer.parseInt(command[2]);
        Optional<Product> p = productManager.getProduct(id);
       return p.isPresent();
    }

}

