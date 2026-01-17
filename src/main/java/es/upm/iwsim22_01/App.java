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
import es.upm.iwsim22_01.service.service.*;

/**
 * Clase principal de la aplicación de gestión de tickets.
 * Esta clase inicializa los gestores y el despachador de comandos,
 * y proporciona un bucle interactivo para procesar comandos de usuario.
 */
public class App {
    private static boolean menu = true;
    private static CommandDispatcher dispatcher = new CommandDispatcher();

    private static ProductService productService = new ProductService();
    private static TicketService ticketService = new TicketService(productService);
    private static CashierService cashierService = new CashierService(ticketService);
    private static ClientService clientService = new ClientService(cashierService, ticketService);

    /**
     * Método principal de la aplicación.
     * Registra los manejadores de comandos y procesa las entradas del usuario.
     *
     * @param args Argumentos de línea de comandos. Si se proporciona un archivo, se ejecutarán los comandos desde él.
     */
    public static void main(String[] args) {
        // Registrar manejadores de comandos
        dispatcher.addCommand("exit", new ExitCommandHandler());
        dispatcher.addCommand("help", new HelpCommandHandler());
        dispatcher.addCommand("echo", new EchoCommandHandler());
        dispatcher.addCommand("prod", new ProdCommandHandler(productService));
        dispatcher.addCommand("client", new ClientCommandHandler(clientService, cashierService));
        dispatcher.addCommand("cash", new CashierCommandHandler(cashierService, clientService, ticketService));
        dispatcher.addCommand("ticket", new TicketCommandHandler(ticketService, productService, cashierService, clientService));

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        // Procesar comandos desde archivo si se proporciona
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

        // Procesar comandos desde consola
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

    /**
     * Finaliza el bucle principal del menú.
     */
    public static void exitMenu() {
        menu = false;
    }
}

