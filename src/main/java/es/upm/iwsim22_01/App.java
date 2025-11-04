package es.upm.iwsim22_01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.commands.EchoCommandHandler;
import es.upm.iwsim22_01.commands.commands.ExitCommandHandler;
import es.upm.iwsim22_01.commands.commands.HelpCommandHandler;
import es.upm.iwsim22_01.commands.commands.ProdCommandHandler;
import es.upm.iwsim22_01.commands.commands.TicketCommandHandler;
import es.upm.iwsim22_01.factory.CashierFactory;
import es.upm.iwsim22_01.manager.*;
import es.upm.iwsim22_01.models.Ticket;

public class App {
    private static boolean menu = true;
    private static Ticket ticket = new Ticket();
    private static CommandDispatcher dispatcher = new CommandDispatcher();

    private static ProductManager productManager = new ProductManager();
    private static ClientManager clientManager = new ClientManager();
    private static TicketManager ticketManager = new TicketManager();
    private static CashierManager cashierManager = new CashierManager();

    private static CashierFactory cashierFactory = new CashierFactory(cashierManager);

    public static void main(String[] args) {
        dispatcher.addCommand("exit", new ExitCommandHandler());
        dispatcher.addCommand("help", new HelpCommandHandler());
        dispatcher.addCommand("echo", new EchoCommandHandler());
        dispatcher.addCommand("prod", new ProdCommandHandler());
        dispatcher.addCommand("ticket", new TicketCommandHandler());

        Scanner scanner;
        if (args.length >= 1) {
            try {
                scanner = new Scanner(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.err.println("Fichero " + args[0] + " no encontrado.");
                scanner = new Scanner(System.in);
            }
        } else {
            scanner = new Scanner(System.in);
        }

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        while (menu) {
            System.out.print("tUPM> ");
            dispatcher.processCommand(scanner.nextLine());

            System.out.println();
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

    public static boolean existsTicket() {
        return ticket != null;
    }
}

