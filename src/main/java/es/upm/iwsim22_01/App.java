package es.upm.iwsim22_01;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.handler.CommandHandler;

import es.upm.iwsim22_01.models.Ticket;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static boolean menu = true;
    private static Ticket ticket = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Ticket ticket = new Ticket();

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

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
                        break; // REVISAR BREAK DENTRO DE IF CUIDADIN
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

    public static void exitMenu() {
        menu = false;
    }

    public static Ticket getCurrentTicket() {
        return ticket;
    }

    public static void resetTicket() {
        ticket = new Ticket();
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

