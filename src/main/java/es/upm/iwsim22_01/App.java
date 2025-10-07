package es.upm.iwsim22_01;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.handler.CommandHandler;

import es.upm.iwsim22_01.manager.ProductManager;
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
        Ticket ticket = new Ticket();

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        while (menu) {
            CommandStatus commandStatus = CommandHandler.runCommand(scanner.nextLine());
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

