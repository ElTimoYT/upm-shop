package es.upm.iwsim22_01;

import es.upm.iwsim22_01.manager.ProductManager;

import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Iterator;
import java.util.Optional;

import java.util.Scanner;

public class App {
    private final static ProductManager PRODUCT_MANAGER = new ProductManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Ticket ticket = new Ticket();

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        boolean menu = true;
        String[] command;
        int id = 0;
        int quantity = 0;
        while (menu) {
            command = scanner.nextLine().split(" ");

            switch (command[0]) {
                case "exit":
                    menu = false;
                    break;
                case "echo":
                    echoMenu(command);
                    break;
                case "help":
                    helpMenu();
                    break;
                case "prod":
                    switch (command[1]) {
                        case "add":
                            System.out.println("prod add: " + (addProductCommand(command)?"ok":"fail"));
                            break;
                        case "list":
                            System.out.println("prod list: " + (listProductsCommand(command)?"ok":"fail"));
                            break;
                        case "update":
                            System.out.println("prod update: " + (updateProductCommand(command)?"ok":"fail"));
                            break;
                        case "remove":
                            System.out.println("prod remove: " + (removeProductCommand(command)?"ok":"fail"));
                            break;
                    }
                    break;
                case "ticket":
                    if (command.length < 2) {
                        System.out.println("Use: ticket <add|remove|new|print>");
                        break;
                    }
                    switch (command[1]) {
                        case "add":
                            if (command.length < 3) {
                                System.out.println("Use: ticket add <ProdID> <Quantity>");
                                break;
                            }
                            if(addTicket(command, PRODUCT_MANAGER, ticket)){
                                System.out.println(ticket);
                                System.out.println("Ticket add: ok");
                            }
                            break;

                        case "remove":
                            if (command.length < 3) {
                                System.out.println("Use: ticket remove <ProdID>");
                                break;
                            }
                            if (idExists(command, PRODUCT_MANAGER)) {
                                ticket.removeProductById(id);
                                System.out.println(ticket);
                                System.out.println("Ticket remove: ok");
                            } else {
                                System.out.println("Product not found");
                            }
                            break;

                        case "new":
                            ticket = new Ticket();
                            System.out.println("Ticket new: ok");
                            break;

                        case "print":
                            System.out.println(ticket);
                            System.out.println("Ticket print: ok");
                            break;
                    }
                    break;
            }
        }

        System.out.println("""
                Closing application.
                Goodbye!
                """);
    }

    private static boolean removeProductCommand(String[] command) {
        int id = stringToInt(command[2]);
        if (id == Integer.MIN_VALUE) {
            return false;
        }

        return App.PRODUCT_MANAGER.removeProduct(id);
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
    }

    public static void echoMenu(String[] args) {
        for (int i = 1; i < args.length; i++) {
            System.out.print(args[i]);
            System.out.print(" ");
        }

        System.out.println();
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


    private static int stringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            System.err.println("No se ha podido convertir '" + string + "'a número entero.");
        }

        return Integer.MIN_VALUE;
    }

    private static double stringToDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            System.err.println("No se ha podido convertir '" + string + "'a número decimal.");
        }

        return Double.MIN_VALUE;
    }

    private static Category stringToCategory(String string) {
        try {
            return Category.valueOf(string);
        } catch (IllegalArgumentException exception) {
            System.err.println("No se ha podido convertir '" + string + "'a categoria.");
        }

        return null;
    }

    public static boolean addTicket(String[] command, ProductManager productManager, Ticket ticket) {

        int id = Integer.parseInt(command[2]);
        int quantity = Integer.parseInt(command[3]);

        Optional<Product> product = productManager.getProduct(id);
        if (product.isPresent()) {
            return ticket.addProduct(product.get(), quantity);
        } else {
            System.out.println("ticket add: error - product id " + id + " does not exist.");
            return false;
        }
    }

    public static boolean idExists(String[] command, ProductManager productManager) {

        int id = Integer.parseInt(command[2]);
       return productManager.getProduct(id).isPresent();
    }

}

