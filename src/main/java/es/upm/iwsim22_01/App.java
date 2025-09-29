package es.upm.iwsim22_01;

import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Optional;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager manager = new ProductManager();
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
                            id = Integer.parseInt(command[2]);
                            quantity = Integer.parseInt(command[3]);
                            if(addTicket(id, quantity, manager, ticket)){
                                System.out.println(ticket);
                                System.out.println("Ticket add: ok");
                            }
                            break;

                        case "remove":
                            if (command.length < 3) {
                                System.out.println("Use: ticket remove <ProdID>");
                                break;
                            }

                            id = Integer.parseInt(command[2]);

                            if (idExists(id, manager)) {
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

    public static boolean addTicket(int id, int quantity, ProductManager productManager, Ticket ticket) {
        boolean exists = idExists(id,  productManager);
        Optional<Product> product = productManager.getProduct(id);

        if (exists) {
            return ticket.addProduct(product.get(), quantity);
        } else {
            System.out.println("ticket add: error - product id " + id + " does not exist.");
            return false;
        }
    }

    public static boolean idExists(int id, ProductManager productManager) {
        Optional<Product> p = productManager.getProduct(id);
       return p.isPresent();
    }


}

