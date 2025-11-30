package es.upm.iwsim22_01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.handlers.EchoCommandHandler;
import es.upm.iwsim22_01.commands.handlers.ExitCommandHandler;
import es.upm.iwsim22_01.commands.handlers.HelpCommandHandler;
import es.upm.iwsim22_01.commands.handlers.cashier.CashierCommandHandler;
import es.upm.iwsim22_01.commands.handlers.client.ClientCommandHandler;
import es.upm.iwsim22_01.commands.handlers.prod.ProdCommandHandler;
import es.upm.iwsim22_01.commands.handlers.ticket.TicketCommandHandler;
import es.upm.iwsim22_01.manager.*;

public class App {
    private static boolean menu = true;
    private static CommandDispatcher dispatcher = new CommandDispatcher();

    private static ProductManager productManager = new ProductManager();
    private static CashierManager cashierManager = new CashierManager();
    private static ClientManager clientManager = new ClientManager(cashierManager);
    private static TicketManager ticketManager = new TicketManager(clientManager, cashierManager);

    public static void main(String[] args) {
        dispatcher.addCommand("exit", new ExitCommandHandler());
        dispatcher.addCommand("help", new HelpCommandHandler());
        dispatcher.addCommand("echo", new EchoCommandHandler());
        dispatcher.addCommand("prod", new ProdCommandHandler(productManager));
        dispatcher.addCommand("client", new ClientCommandHandler(clientManager, cashierManager));
        dispatcher.addCommand("cash", new CashierCommandHandler(cashierManager, ticketManager));
        dispatcher.addCommand("ticket", new TicketCommandHandler(ticketManager, productManager, cashierManager, clientManager));

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        Scanner scanner;
        if (args.length >= 1) {
            try {
                scanner = new Scanner(new FileReader(args[0]));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("tUPM> " + line);
                    dispatcher.processCommand(line);
                    System.out.println();
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("Fichero " + args[0] + " no encontrado.");
            }
        }

        scanner = new Scanner(System.in);
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
}

